/*
 * ConfigMaster default page javascript
 * http://ConfigMaster/
 *
 * Copyright 2014 Microsoft Corporation
 *
 * Date: 2014-08-24T23:05Z
 */

 var keywords="";

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

    CM.searchBox = {
        $container: $('#search-box'),
        init: function () {
            var me = this;
            this.search = CM.search;
            this.search.initSearchElements.call(this);
            this.$error = this.$container.find('p');
            this.$web.click(function () {
                CM.search.type = 0;
                $('#web').css("background-color","#62a29e");
                $('#forum').css("background-color","#69aaaa");
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
                $('#web').css("background-color","#62a29e");
                $('#forum').css("background-color","#69aaaa");
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
        renderList: function (type) {

            //var List = CM.search.data.qrfd.contentList;
            var List = CM.search.data.dataMap.Renmin_University_of_China.wikiInfo;
            var html = '<div id="sum">当前共有'+CM.search.indexCount+'条搜索结果</div>';
            if (type == 0) {
                for (var i = 0; i < List.length; i++) { //第二层循环取list中的对象
                    var excerpt = null;
                    excerpt = List[i];
                    html += '<div class="result-item"><div id="title"><a href="' + List[i] + '" target="_blank">' + List[i] + '</a></div><div class="resource">引自：' + List[i] + '</div><div class="excerpt">摘要：<span>' + excerpt + '.....</span></div><hr />';

                }


                //for (var i = 0; i < List.length; i++) { //第二层循环取list中的对象
                //    var excerpt = null;
                //    excerpt = List[i].content.substring(0, 50);
                //    if (List[i].date == null) {
                //        html += '<div class="result-item"><div id="title"><a href="' + List[i].url + '" target="_blank">' + List[i].title + '</a></div><div class="resource">引自：' + List[i].plate + '</div><div class="excerpt">摘要：<span>' + excerpt + '.....</span></div><hr />';
                //    }
                //
                //    else {
                //        html += '<div class="result-item"><div id="title"><a href="' + List[i].url + '" target="_blank">' + List[i].title + '</a></div><div class="resource">引自：' + List[i].plate + '日期：' + List[i].date + '</div><div class="excerpt">摘要：<span>' + excerpt + '.....</span></div><hr />';
                //    }
                //}
            }
            else {
                for (var i = 0; i < List.length; i++) { //第二层循环取list中的对象
                    var excerpt = null;
                    excerpt = List[i];
                    if (List[i].time == null) {
                        html += '<div class="result-item"><div id="title"><a href="' + List[i] + '" target="_blank">' + List[i] + '</a></div><div class="resource">来源：' + List[i] + '</div></div><hr />';
                    }
                    else {
                        html += '<div class="result-item"><div id="title"><a href="' + List[i] + '" target="_blank">' + List[i] + '</a></div><div class="resource">来源：' + List[i] + '</div><div class="date">日期：<span>' + List[i] + '<div class="excerpt">摘要：<span>' + excerpt + '.....</span></div><hr />';
                    }
                }
            }
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
        type:0,
        data:null,
        
        initSearchElements: function () {
            this.$input = this.$container.find('input');
            //this.$searchBtn = this.$container.find('#s');
            this.$searchBtn = this.$container.find('#s');
            this.$web = this.$container.find('#web');
            this.$forum = this.$container.find('#forum');
        },

        onSearchBtnClick: function (me) {
            var query = $.trim(me.$input.val());
            keywords=query;
            if (query.length) {
                //me.$searchBtn.addClass('loading');
                this.request(query, 1,this.type);
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

        request: function (query, start,type) {
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
                            CM.resultList.renderList(type);
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

        enter_detail:function(){
            //发出了keywords
            window.open("http://58.198.176.141:8080/EduAnalysis/searchEventForDisplay?q=" + keywords + "&topN=200", "_blank");
        },
        jump: function (pageNumber) {
            CM.content.showLoading();
            this.request(this.query, (pageNumber - 1) * 10,this.type);
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


/*function drawPie(map){
	 var legends=new Array();
	 var sections = new Array();
	 var i=0;
	 for(var index in map){
		 i++;
		 legends.push({});
		 sections.push({});
		 legends[i] = index;
		 sections[i] = {"name":index,"value":map[index]};
	 }
	 var myChart = echarts.init(document.getElementById('pie'));
	 option = {
			    title : {
			        text: '数据源',
			        subtext: '数据源分布',
			        x:'center'
			    },
			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c} ({d}%)"
			    },
			    legend: {
			        orient : 'vertical',
			        x : 'left',
//			        data:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎']
			        data : legends
			    },
			    toolbox: {
			        show : true,
			        feature : {
			            mark : {show: true},
			            dataView : {show: true, readOnly: false},
			            magicType : {
			                show: true, 
			                type: ['pie', 'funnel'],
			                option: {
			                    funnel: {
			                        x: '25%',
			                        width: '50%',
			                        funnelAlign: 'left',
			                        max: 1548
			                    }
			                }
			            },
			            restore : {show: true},
			            saveAsImage : {show: true}
			        }
			    },
			    calculable : true,
			    series : [
			        {
			            name:'新闻来源',
			            type:'pie',
			            radius : '55%',
			            center: ['50%', '60%'],
//			            data:[
//			                {value:335, name:'直接访问'},
//			                {value:310, name:'邮件营销'},
//			                {value:234, name:'联盟广告'},
//			                {value:135, name:'视频广告'},
//			                {value:1548, name:'搜索引擎'}
//			            ]
			            data :sections
			        }
			    ]
			};
	        myChart.setOption(option);
}

function drawLabelCloud(data){
	
}*/