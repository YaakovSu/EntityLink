/*
 * ConfigMaster JavaScript common library
 * http://ConfigMaster/
 *
 * Copyright 2014 Microsoft Corporation
 *
 * Date: 2014-08-24T22:19Z
 */
/*animation for top-menu*/


String.prototype.format = function () {
    for (var str = this, n = 0; n < arguments.length; ++n)
        str = str.replace(new RegExp("\\{" + n + "\\}", "g"), arguments[n]);
    return str
};

String.prototype.encodeHTML = function () {
    return this.replace(/&/g, '&amp;').replace(/"/g, '&quot;').replace(/'/g, '&#39;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
};

//binding the "enter" with search-btn

window.CM = {};

// Template
CM.templates = {};

CM.template = {
    obj: function (data, name) {
        this.template = data;
        this.name = name;
    },
    get: function (name) {
        return CM.templates[name] || this.init(name);
    },
    init: function (name) {
        var me = this;
        var templates = CM.templates;
        $.ajax({
            url: 'templates/{0}.html?rnd={1}'.format(name, new Date().getTime()),
            async: false,
            success: function (data) {
                templates[name] = new me.obj(data, name);
            },
            error: function (error) {
            }
        });
        return templates[name];
    }
};

CM.template.obj.prototype.render = function (data) {
    var splitArr = this.template.split('@');
    var i, replacement;
    var numberAndLetterExp = /^[0-9a-zA-Z]+$/;
    if (splitArr.length > 1) {
        for (i = 1; i < splitArr.length; i++) {
            if (splitArr[i] === '') {
                splitArr[i] = '@';
                i = i + 1;
            } else {
                replacement = findReplacement(splitArr[i]);
                if (replacement) {
                    splitArr[i] = splitArr[i].replace(replacement.target, replacement.replacement);
                }
            }
        }
    }
    return splitArr.join('');
    function Replacement(target, replacement) {
        return {
            target: target,
            replacement: replacement
        };
    }

    function KeyPair(key, value) {
        return {
            key: key,
            value: value
        };
    }

    function findReplacement(str) {
        var firstChar = str.substr(0, 1);
        var assumptionKey, keyPair;
        if (numberAndLetterExp.test(firstChar)) {
            keyPair = findValidKey(str);
            if (keyPair) {
                return new Replacement(keyPair.key, keyPair.value.encodeHTML());
            } else {
                return new Replacement(str, '@{0}'.format(str));
            }
        } else if (firstChar === '{') {
            assumptionKey = str.substr(1, str.indexOf('}') - 1)
            var keyPair = validateAssumptionKey(assumptionKey);
            if (keyPair) {
                return new Replacement('{{0}}'.format(keyPair.key), keyPair.value);
            }
        } else if (firstChar === '(') {
            assumptionKey = str.substr(1, str.indexOf(')') - 1)
            var keyPair = validateAssumptionKey(assumptionKey);
            if (keyPair) {
                return new Replacement('({0})'.format(keyPair.key), keyPair.value.encodeHTML());
            }
        } else if (firstChar === '') {
            return new Replacement('', '@');
        } else {
            return new Replacement(str, '@{0}'.format(str));
        }
        return false;
    }

    function validateAssumptionKey(key) {
        var keySplitArr = key.split('.');
        var source = data;
        for (var i = 0; i < keySplitArr.length; i++) {
            if (keySplitArr[i] in source) {
                source = source[keySplitArr[i]];
            } else {
                break;
            }
        }
        if (i === keySplitArr.length && isPrintable(source)) {
            return new KeyPair(key, source.toString());
        } else {
            return false;
        }
    }

    function findValidKey(str) {
        var key;
        for (var i = 1; i <= str.length; i++) {
            key = str.substr(0, i);
            if (!numberAndLetterExp.test(key)) {
                return false;
            }
            if (key in data && isPrintable(data[key])) {
                return new KeyPair(key, data[key].toString());
            }
        }
        return false;
    }

    function isPrintable(value) {
        var objType = Object.prototype.toString.call(value).toLowerCase();
        if (objType === '[object string]' || objType === '[object number]') {
            return true;
        } else {
            return false;
        }
    }
};