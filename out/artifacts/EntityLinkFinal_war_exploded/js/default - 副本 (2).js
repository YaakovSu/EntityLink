/*
 * ConfigMaster default page javascript
 * http://ConfigMaster/
 *
 * Copyright 2014 Microsoft Corporation
 *
 * Date: 2014-08-24T23:05Z
 */

var keywords = "";

(function () {
    var CM = window.CM;
    var $window = $(window);


    $(function () {
        CM.searchBox.init();
        CM.topSearch.init();
        CM.searchBox.$input.trigger('focus');

    });
    $(document).keypress(function (e) {
        if (e.which == 13) {
            $searchButton.click();
        }
    });
    $(document).ready(function () {
        $(".title1").click(function () {
            $(".sub-table1").slideToggle("fast");
        });
        $(".title2").click(function () {
            $(".sub-table2").slideToggle("fast");
        });
        $(".title3").click(function () {
            $(".sub-table3").slideToggle("fast");
        });
        $(".title4").click(function () {
            $(".sub-table4").slideToggle("fast");
        });
        $(".title5").click(function () {
            $(".sub-table5").slideToggle("fast");
        });
    });

    CM.searchBox = {
        $container: $('#search-box'),
        init: function () {
            var me = this;
            this.search = CM.search;
            this.search.initSearchElements.call(this);
            this.$error = this.$container.find('p');
            this.$web.click(function () {
                CM.search.type = 0;
                $('#web').css("background-color", "#62a29e");
                $('#forum').css("background-color", "#69aaaa");
            });
            /*this.$forum.click(function () {
             CM.search.type = 1;
             $('#forum').css("background-color","#62a29e");
             $('#web').css("background-color","#69aaaa");
             });*/
            this.$searchBtn.click(function () {
                me.hideError();
                me.search.onSearchBtnClick(me);
            });
        },
        showError: function (msg) {
            this.$error.text(msg).fadeIn(220);
        },
        hideError: function () {
            this.$error.fadeOut(220);
        }
    };

    CM.topSearch = {
        $container: $('#top-search'),
        init: function () {
            var me = this;
            this.search = CM.search;
            this.search.initSearchElements.call(this);
            this.$web.click(function () {
                CM.search.type = 0;
                $('#web').css("background-color", "#62a29e");
                $('#forum').css("background-color", "#69aaaa");
            });
            /*this.$forum.click(function () {
             CM.search.type = 1;
             $('#web').css("background-color","#62a29e");
             $('#forum').css("background-color","#69aaaa");
             });*/
            this.$searchBtn.click(function () {
                if (me.search.onSearchBtnClick(me)) {
                    CM.content.showLoading();
                }
            });
        }
    };

    CM.content = {
        $container: $('#content'),
        $err: $('#content p'),
        $loading: $('#content .loading'),
        init: function () {
            var me = this;
            this.resetHeight();
            $window.resize(function () {
                me.resetHeight();
            });
        },
        resetHeight: function () {
            this.$container.fadeIn(220);
            this.$container.css('min-height', '{0}px'.format($window.outerHeight() - CM.topSearch.$container.outerHeight()));
        },
        showError: function (msg) {
            this.refresh();
            this.$err.text(msg).show();
        },
        hideError: function () {
            this.$err.hide();
        },
        showLoading: function () {
            this.refresh();
            this.$loading.show();
        },
        hideLoading: function () {
            this.$loading.hide();
        },
        refresh: function () {
            CM.resultList.refresh();
            CM.pager.refresh();
            this.hideError();
            this.hideLoading();
        }
    };

    CM.resultList = {
        $container: $('#result-list'),
        init: function () {
            this.$container = $('<div id="result-list"></div>').appendTo(CM.content.$container);
            //this.template = CM.template.get('ResultListItem');
        },
        //renderList: function (items) {
        //    var html = '';
        //    for (var i = 0; i < items.length; i++) {
        //        html += this.template.render(items[i]);
        //    }
        //    this.$container.html(html);
        //},
        /*renderList: function (type) {

         var List = CM.search.data.contentList;
         var html = '<div id="sum">当前共有'+CM.search.indexCount+'条搜索结果</div>';
         if (type == 0) {
         for (var i = 0; i < List.length; i++) { //第二层循环取list中的对象
         if (List[i].date == null) {
         html += '<div class="result-item"><div id="title"><a href="' + List[i].url + '" target="_blank">' + List[i].title + '</a></div><div class="resource">' + List[i].source + '</div></div><hr />';
         }

         else {
         html += '<div class="result-item"><div id="title"><a href="' + List[i].url + '" target="_blank">' + List[i].title + '</a></div><div class="resource">' + List[i].source + '</div><div class="date">日期：<span>' + List[i].date + '</span></div><hr />';
         }
         }
         }
         else {
         for (var i = 0; i < List.length; i++) { //第二层循环取list中的对象
         if (List[i].time == null) {
         html += '<div class="result-item"><div id="title"><a href="' + List[i].dataSourceUrl + '" target="_blank">' + List[i].title + '</a></div><div class="resource">' + List[i].plate + '</div></div><hr />';
         }
         else {
         html += '<div class="result-item"><div id="title"><a href="' + List[i].dataSourceUrl + '" target="_blank">' + List[i].title + '</a></div><div class="resource">' + List[i].plate + '</div><div class="date">日期：<span>' + List[i].time + '</span></div><hr />';
         }
         }
         }
         this.$container.html(html);
         },*/
        renderList: function () {

            //var List = CM.search.data.qrfd.contentList;
            var List0 = CM.search.data.dataMap.yago0;
            var List1 = CM.search.data.dataMap.yago1;
            var List2 = CM.search.data.dataMap.yago2;
            var count = 0;
            if (typeof(List0) != "undefined") {
                count = count + 1;
            }
            if (typeof(List1) != "undefined") {
                count = count + 1;
            }
            if (typeof(List2) != "undefined") {
                count = count + 1;
            }
            var html = '<div id="sum">当前共有' + count + '条搜索结果</div>';


            var yagoName = List0.name;
            var yagoText = List0.text;
            var yagoWikiInfo = List0.wikiInfo;
            var yagoResult = List0.result;
            var yagoType = List0.type;




            html+='<div class="container-fluid">'
            html+='<div class="row-fluid">'
            html+='<div class="span12">'
            html+=' <span class="label badge-success">展示1</span>'
            html+='<div class="row-fluid">'
            html+='<div class="span8">'

            html+=' <div class="hero-unit well">'
            html+=' <h3>'
            html+=yagoName
            html+=' </h3>'
            html+='<p>'
            html+=yagoText.substring(0,50)+'...'
            html+='</p>'
            html+='<p>'
            html+='<a class="btn" href = "javascript:void(0)" onclick = "document.getElementById('+"'light'"+').style.display='+"'block'"+';document.getElementById('+"'fade'"+').style.display='+"'block'"+'">查看更多>></a>'
            html+=' </p>'
            html+='<div id="light" class="white_content">'
            html+='  <a class="btn" href = "javascript:void(0)" onclick = "document.getElementById('+"'light'"+').style.display='+"'none'"+';document.getElementById('+"'fade'"+').style.display='+"'none'"+'">点这里关闭本窗口</a>'
            html+='  <p>'
            html+=yagoText
            html+='  </p>'
            html+='</div>'
            html+=' <div id="fade" class="black_overlay">'
            html+=' </div>'
            html+='</div>'
            html+='<div id="tree-container"></div>'
            html+='  </div><span class="label badge-success">展示2</span>'

            html+=' <div class="span4">'
            html+='  <div id="list" style="text-align: right" align="right">'
            html+='<h3 align="right">'+yagoName+"关系子图"+'</h3>'
            html+=' <table id="mytable" cellspacing="0" style="margin: auto">'
            html+=' <caption></caption>'
            html+='<tr class="title1">'
            html+=' <th scope="col">一</th>'
            html+=' <th scope="col">LinkTo</th>'
            html+=' </tr>'
            for(var i=0;i<yagoWikiInfo.length;i++){
                html+=' <tr class="sub-table1">'
                html+='  <td class="row">'+i+'</td>'
                html+=' <td class="row">'+yagoWikiInfo[i]+'</td>'
                html+='</tr>'
            }

            html+=' <tr class="title2">'
            html+=' <th scope="col">二</th>'
            html+='  <th scope="col">Type</th>'
            html+=' </tr>'
            for(var i=0;i<yagoType.length;i++){
                html+=' <tr class="sub-table2">'
                html+='  <td class="row">'+i+'</td>'
                html+=' <td class="row">'+yagoType[i]+'</td>'
                html+='</tr>'
            }

            html+=' <tr class="title3">'
            html+='<th scope="col">三</th>'
            html+='<th scope="col">WikiInfo</th>'
            html+='</tr>'
            for(var i=0;i<yagoResult.length;i++){
                html+=' <tr class="sub-table3">'
                html+='  <td class="row">'+i+'</td>'
                html+=' <td class="row">'+yagoResult[i]+'</td>'
                html+='</tr>'
            }
            html+='</table>'
            html+='</div>'
            html+='</div>'



            html+='</div> <span class="label badge-success">展示3</span>'
            html+='<div class="row-fluid">'
            html+=' <div class="span12">'

            html+='<div id="list"  style="text-align:center">'
            html+='  <h3>反馈</h3>'
            html+='<table id="mytable" cellspacing="0" style="margin: auto">'
            html+='<caption></caption>'
            html+=' <tr class="title4">'
            html+=' <th scope="col">一</th>'
            html+=' <th scope="col">候选YAGO实体</th>'
            html+='  </tr>'

            var cout = 1;
            if (typeof(List1) != "undefined"){
                yagoName = List1.name;
                if(List1.name!=""){
                    html+=' <tr class="sub-table4">'
                    html+=' <td class="row">'+cout+'</td>'
                    html+=' <td class="row">'+yagoName+'</td>'
                    html+='</tr>'
                    cout+=1;
                }

            }
            if(typeof(List2) != "undefined"){
                yagoName = List2.name;
                if(yagoName!=""){
                    html+='<tr class="sub-table4">'
                    html+=' <td class="row">'+cout+'</td>'
                    html+=' <td class="row">'+yagoName+'</td>'
                    html+='</tr>'
                }

            }
            html+='</table>'
            html+=' </div>'
            html+=' </div>'
            html+='</div>'
            html+=' </div>'
            html+=' </div>'
            html+='</div>'





            this.$container.html(html);
        },
        refresh: function () {
            this.$container.html('');
        }
    };

    CM.pager = {
        init: function () {
            this.$container = $('<div id="pager"></div>').appendTo(CM.content.$container).on('mouseover', 'li', function () {
                $(this).addClass('hover');
            }).on('mouseout', 'li', function () {
                $(this).removeClass('hover');
            }).on('click', 'li', function () {
                var $this = $(this);
                var search = CM.search;
                if ($this.hasClass('prev')) {
                    search.prev();
                } else if ($this.hasClass('next')) {
                    search.next();
                } else if (!$this.hasClass('current')) {
                    search.jump(Number($this.text()));
                }
            });
        },
        renderPager: function (pageNumber, pageCount) {
            var $ul = $('<ul></ul>'), $li, i;
            if (pageCount > 1) {
                if (pageNumber > 1) {
                    $li = new PageButton().addClass('prev').appendTo($ul);
                }
                for (i = pageNumber - 4; i < pageNumber; i++) {
                    if (i > 0) {
                        $li = new PageButton(i).appendTo($ul);
                    }
                }
                $li = new PageButton(pageNumber).addClass('current').appendTo($ul);
                for (i = pageNumber + 1; i < pageNumber + 5; i++) {
                    if (i <= pageCount) {
                        $li = new PageButton(i).appendTo($ul);
                    }
                }
                if (pageNumber != pageCount) {
                    $li = new PageButton().addClass('next').appendTo($ul);
                }
            }
            this.$container.html($ul);
            function PageButton() {
                return $('<li class="page-btn">{0}</li>'.format(arguments[0] ? arguments[0] : ''));
            }
        },
        refresh: function () {
            this.$container.html('');
        }
    };

    CM.search = {
        query: '',
        pageNumber: 1,
        pageCount: 1,
        pageSize: 10,
        index: 1,
        indexCount: 1,
        initialized: false,
        noResultMsg: '没有匹配当前搜索词的结果',
        requestErrorMsg: '请求失败',
        type: 0,
        data: null,

        initSearchElements: function () {
            this.$input = this.$container.find('input');
            //this.$searchBtn = this.$container.find('#s');
            this.$searchBtn = this.$container.find('#s');
            this.$web = this.$container.find('#web');
            this.$forum = this.$container.find('#forum');
        },

        onSearchBtnClick: function (me) {
            var query = $.trim(me.$input.val());
            keywords = query;
            if (query.length) {
                //me.$searchBtn.addClass('loading');
                this.request(query, 1, this.type);
                return true;
            }
        },

        showExceptionMsg: function (msg) {
            if (this.initialized) {
                CM.content.showError(msg);
            } else {
                CM.searchBox.showError(msg);
            }
        },

        request: function (query, start, type) {
            var me = this;
            var link = null;
            if (type == 0) {
                link = 'http://localhost:8080/SearchAction';
            }
            /*else {
             link = 'http://localhost:8080/NewsSearch/searchForum';
             }*/
            $.ajax({
                url: link,   // url：  search for website
//                    url: '/NewsSearch/searchForum',   // url：  search for forum 
                dataType: 'json',
                data: {
                    q: query,
                    start: start
                },
                success: function (data) {
                    console.log(data);
                    me.data = data;
                    //me.pageNumber = data.qrfd.pageNumber+1;
                    //me.indexCount = data.qrfd.recordCount;
                    me.index = start;
                    //me.pageCount = Math.ceil(me.indexCount / me.pageSize);
                    me.query = query;
                    if (me.pageNumber === 0) {
                        me.showExceptionMsg(me.noResultMsg);
                    } else {
                        if (!me.initialized) {
                            CM.topSearch.$input.val(query);
                            CM.searchBox.$container.fadeOut(220, function () {
                                CM.searchBox.$container.remove();
                                CM.topSearch.$container.slideDown(220, function () {
                                    CM.content.init();
                                });
                            });
                            //drawPie(data.staticsMap);
                            CM.resultList.init();
                            CM.pager.init();
                            me.initialized = true;
                        }
                        CM.content.refresh();
                        CM.pager.renderPager(me.pageNumber, me.pageCount);
                        //CM.resultList.renderList(data.contentList);
                        CM.resultList.renderList();

                        $(".title1").click(function () {
                            $(".sub-table1").slideToggle("fast");
                        });
                        $(".title2").click(function () {
                            $(".sub-table2").slideToggle("fast");
                        });
                        $(".title3").click(function () {
                            $(".sub-table3").slideToggle("fast");
                        });
                        $(".title4").click(function () {
                            $(".sub-table4").slideToggle("fast");
                        });
                        $(".title5").click(function () {
                            $(".sub-table5").slideToggle("fast");
                        });


                    }
                },
                error: function () {
                    me.showExceptionMsg(me.requestErrorMsg);
                },
                complete: function () {
                    $('button.loading').removeClass('loading');
                }
            });
        },

        enter_detail: function () {
            //发出了keywords
            window.open("http://58.198.176.141:8080/EduAnalysis/searchEventForDisplay?q=" + keywords + "&topN=200", "_blank");
        },
        jump: function (pageNumber) {
            CM.content.showLoading();
            this.request(this.query, (pageNumber - 1) * 10, this.type);
        },
        prev: function () {
            if (this.pageNumber > 1) {
                this.jump(this.pageNumber - 1, this.type);
            }
        },
        next: function () {
            if (this.pageNumber < this.pageCount) {
                this.jump(this.pageNumber + 1, this.type);
            }
        }
    }
})();
