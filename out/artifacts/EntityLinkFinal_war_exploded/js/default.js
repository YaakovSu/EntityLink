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


            html+='<div role="navigation" id="foo" class="nav-collapse">'
            html+='<ul>'
            html+='<li class="active" onclick="changeHome()"><a href="#home" id="home">英文维基</a></li>'
            html+='<li><a href="#about" id="about" onclick="changeAbout()">中文维基</a></li>'
            html+='<li><a href="#projects" id="projects" onclick="changeProjects()">YAGO关系图</a></li>'
            html+='<li><a href="#blog" id="blog" onclick="changeBlog()">相关分析</a></li>'
            html+='</ul>'
            html+='</div>'
            html+='<div class="main" id="tt1">'
            html+='<a href="#nav" class="nav-toggle">Menu</a>'
            html+='<article>'
            html+='<h1><font color="#5B5B5B">'+yagoName+'</font></h1>'
            html+=yagoText
            html+='</article>'

            html+='<br>'
            html+='<HR style="border:3 '+'double #000000" width="100%" color=#000000 SIZE=3>'
            html+='<br>'

            html+='<article>'
            html+='<h1><font color="#5B5B5B">'+yagoName+'</font></h1>'
            html+=yagoText
            html+='</article>'

            html+='</div>'
            
            html+='<div class="" id="tt2" style="display: none;">'

            html+='<div id="tree-container" style="text-align:center">'
              html+='              <div id="footer">'
             html+='                   YAGO关系树'
             html+='                   <div class="hint">'
             html+='                       yagoName'
            html+='                    </div>'
             html+='               </div>'
html+='</div>'
html+='</div>'
html+='<div class="" id="tt3" style="display: none;">'
 html+='<div id="ciyun-container">'
            html+='<h1><font color="#5B5B5B">关键词词云</font></h1>'
        html+='</div>'
             html+='<br>'
html+='<HR style="border:3 '+'double #000000" width="100%" color=#000000 SIZE=3>'
html+='<br>'
       html+=' <div id="stcq2" class="cixingboxt" style="display: block;">'
        html+='<h1><font color="#5B5B5B">关键句</font></h1>'
                        html+='<textarea class="input">展示英文页面</textarea>'
        html+='</div>'
html+='</div>'
html+='<div  class="" id="tt4" style="display: none;">'
html+='</div>'
    html+='</div>'

            html+='<div class="di">'
            html+='<div class="caidan">'
            html+='<div class="kuai01">'
            html+=' <div class="b1">相关网站</div>'
            html+=' <div class="b2">'
            html+=' <div class="b3">'
            html+=' <a href="http://www.nlpir.org/" target="_black">NLPIR共享平台</a>'
            html+=' </div>'

            html+='<div class="b3">'
            html+=' <a href="http://ictclas.nlpir.org/" target="_black">ICTCLAS分词系统</a>'
            html+=' </div>'

            html+=' <div class="b3">'
            html+=' <a href="http://www.bigdatabbs.com/" target="_black">大数据论坛</a>'
            html+='</div>'
            html+=' </div>'
            html+=' </div>'

            html+=' <div class="kuai02">'
            html+=' <div class="b1">资源共享</div>'
            html+=' <div class="b2">'
            html+='  <div class="b3">'
            html+=' <a href="http://www.bigdatabbs.com/forum.php?mod=forumdisplay&amp;fid=50" target="_black">技术文档</a>'
            html+='</div>'

            html+=' <div class="b3">'
            html+='<a href="https://github.com/NLPIR-team/NLPIR" target="_black">GitHub</a>'
            html+='</div>'

            html+=' <div class="b3">'
            html+=' <a href="http://www.bigdatabbs.com/forum.php?mod=forumdisplay&amp;fid=52" target="_black">共享软件</a>'
            html+=' </div>'
            html+='</div>'
            html+=' </div>'

            html+='<div class="kuai02">'
            html+='<div class="b1">关于NLPIR</div>'
            html+='<div class="b2">'
            html+='<div class="b3">'
            html+='<a href="http://ictclas.nlpir.org/docs" target="_black">NLPIR简介</a>'
            html+='</div>'
            html+=' </div>'
            html+=' </div>'

            html+=' <div class="kuai02">'
            html+=' <div class="b1">关于我们</div>'
            html+=' <div class="b2">'
            html+=' <div class="b3">'
            html+=' <a href="http://ictclas.nlpir.org/contactus" target="_black">联系我们</a>'
            html+='</div>'
            html+=' </div>'
            html+='</div>'

            html+='<div class="kuai03">'
            html+='<div class="b1">微信扫一扫</div>'
            html+='</div>'

            html+='<div class="kuai04">'
            html+='<img src="imagenew/09.jpg" style="width:90px; height:90px;">'
            html+='</div>'
            html+=' </div>'

            html+='<div class="fenjie"></div>'
            html+=' </div>'
            html+=' <div class="row-fluid">'
            html+='   <div class="span12">'
            html+=' </div>'
            html+='</div>'
            html+=' </div>'

            html+='<script>'
            html+='$("article").readmore({maxHeight: 240});'
            html+='</script>'



///////////////////////////////////////////////////////////////////////////////////////////


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
                cache:false,
                async:false,
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


                    //    ********************************************************************


// 8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888

var radius = 180;//3D 球的半径
var dtr = Math.PI/180;
var d=600;

var mcList = [];
var active = false;
var lasta = 1;
var lastb = 1;
var distr = true;
var tspeed=20;//文字移动速度
var size=250;

var mouseX=0;
var mouseY=0;

var howElliptical=1;

var aA=null;
var oDiv=null;
var i=0;
    var oTag=null;
    $.getJSON( "test.json", function( data ) {
        console.log("这里getjson");
        var items = [];
        $.each( data, function( key, val ) {
            items.push( "<a href=#  style=font-size:"+val+"px>"+key+"</a>" );
        });
        $( "<div/>", {
            "id": "div1",
            style:"border:solid 0px black",
            ALIGN: "center",
            html: items.join( "" )
        }).appendTo( "#ciyun-container" );
        //console.log($('div'));

        oDiv=document.getElementById('div1');
        aA=oDiv.getElementsByTagName('a');

        for(i=0;i<aA.length;i++)
        {
            oTag={};
            oTag.offsetWidth=aA[i].offsetWidth;
            oTag.offsetHeight=aA[i].offsetHeight;
            mcList.push(oTag);
        }

        sineCosine( 0,0,0 );

        positionAll();

        oDiv.onmouseover=function ()
        {
            active=true;
        };

        oDiv.onmouseout=function ()
        {
            active=false;
        };

        oDiv.onmousemove=function (ev)
        {
            var oEvent=window.event || ev;

            mouseX=oEvent.clientX-(oDiv.offsetLeft+oDiv.offsetWidth/2);
            mouseY=oEvent.clientY-(oDiv.offsetTop+oDiv.offsetHeight/2);

            mouseX/=5;
            mouseY/=5;
        };

        setInterval(update, 30);
    });

function update()
{
    var a;
    var b;
    
    if(active)
    {
        a = (-Math.min( Math.max( -mouseY, -size ), size ) / radius ) * tspeed;
        b = (Math.min( Math.max( -mouseX, -size ), size ) / radius ) * tspeed;
    }
    else
    {
        a = lasta * 0.98;
        b = lastb * 0.98;
    }
    
    lasta=a;
    lastb=b;
    
    if(Math.abs(a)<=0.01 && Math.abs(b)<=0.01)
    {
        return;
    }
    
    var c=0;
    sineCosine(a,b,c);
    for(var j=0;j<mcList.length;j++)
    {
        var rx1=mcList[j].cx;
        var ry1=mcList[j].cy*ca+mcList[j].cz*(-sa);
        var rz1=mcList[j].cy*sa+mcList[j].cz*ca;
        
        var rx2=rx1*cb+rz1*sb;
        var ry2=ry1;
        var rz2=rx1*(-sb)+rz1*cb;
        
        var rx3=rx2*cc+ry2*(-sc);
        var ry3=rx2*sc+ry2*cc;
        var rz3=rz2;
        
        mcList[j].cx=rx3;
        mcList[j].cy=ry3;
        mcList[j].cz=rz3;
        
        per=d/(d+rz3);
        
        mcList[j].x=(howElliptical*rx3*per)-(howElliptical*2);
        mcList[j].y=ry3*per;
        mcList[j].scale=per;
        mcList[j].alpha=per;
        
        mcList[j].alpha=(mcList[j].alpha-0.6)*(10/6);
    }
    
    doPosition();
    depthSort();
}

function depthSort()
{
    var i=0;
    var aTmp=[];
    
    for(i=0;i<aA.length;i++)
    {
        aTmp.push(aA[i]);
    }
    
    aTmp.sort
    (
        function (vItem1, vItem2)
        {
            if(vItem1.cz>vItem2.cz)
            {
                return -1;
            }
            else if(vItem1.cz<vItem2.cz)
            {
                return 1;
            }
            else
            {
                return 0;
            }
        }
    );
    
    for(i=0;i<aTmp.length;i++)
    {
        aTmp[i].style.zIndex=i;
    }
}

function positionAll()
{
    var phi=0;
    var theta=0;
    var max=mcList.length;
    var i=0;
    
    var aTmp=[];
    var oFragment=document.createDocumentFragment();
    
    //�������
    for(i=0;i<aA.length;i++)
    {
        aTmp.push(aA[i]);
    }
    
    aTmp.sort
    (
        function ()
        {
            return Math.random()<0.5?1:-1;
        }
    );
    
    for(i=0;i<aTmp.length;i++)
    {
        oFragment.appendChild(aTmp[i]);
    }
    
    oDiv.appendChild(oFragment);
    
    for( var i=1; i<max+1; i++){
        if( distr )
        {
            phi = Math.acos(-1+(2*i-1)/max);
            theta = Math.sqrt(max*Math.PI)*phi;
        }
        else
        {
            phi = Math.random()*(Math.PI);
            theta = Math.random()*(2*Math.PI);
        }
        //���任
        mcList[i-1].cx = radius * Math.cos(theta)*Math.sin(phi);
        mcList[i-1].cy = radius * Math.sin(theta)*Math.sin(phi);
        mcList[i-1].cz = radius * Math.cos(phi);
        
        aA[i-1].style.left=mcList[i-1].cx+oDiv.offsetWidth/2-mcList[i-1].offsetWidth/2+'px';
        aA[i-1].style.top=mcList[i-1].cy+oDiv.offsetHeight/2-mcList[i-1].offsetHeight/2+'px';
    }
}

function doPosition()
{
    var l=oDiv.offsetWidth/2;
    var t=oDiv.offsetHeight/2;
    for(var i=0;i<mcList.length;i++)
    {
        aA[i].style.left=mcList[i].cx+l-mcList[i].offsetWidth/2+'px';
        aA[i].style.top=mcList[i].cy+t-mcList[i].offsetHeight/2+'px';
        //aA[i].style.fontSize=Math.ceil(12*mcList[i].scale/2)+8+'px';
        aA[i].style.filter="alpha(opacity="+100*mcList[i].alpha+")";
        aA[i].style.opacity=mcList[i].alpha;
    }
}

function sineCosine( a, b, c)
{
    sa = Math.sin(a * dtr);
    ca = Math.cos(a * dtr);
    sb = Math.sin(b * dtr);
    cb = Math.cos(b * dtr);
    sc = Math.sin(c * dtr);
    cc = Math.cos(c * dtr);
}


//8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888

                        console.log(data.dataMap.graphJson);



                            // Calculate total nodes, max label length
                            var treeData = data.dataMap.graphJson
                            var totalNodes = 0;
                            var maxLabelLength = 0;
                            // variables for drag/drop
                            var selectedNode = null;
                            var draggingNode = null;
                            // panning variables
                            var panSpeed = 200;
                            var panBoundary = 20; // Within 20px from edges will pan when dragging.
                            // Misc. variables
                            var i = 0;
                            var duration = 750;
                            var root;

                            // size of the diagram
                            var viewerWidth = $(document).width()/1.5;
                            var viewerHeight = $(document).height()/1.5;

                            var tree = d3.layout.tree()
                                .size([viewerHeight, viewerWidth]);

                            // define a d3 diagonal projection for use by the node paths later on.
                            var diagonal = d3.svg.diagonal()
                                .projection(function(d) {
                                    return [d.y, d.x];
                                });

                            // A recursive helper function for performing some setup by walking through all nodes

                            function visit(parent, visitFn, childrenFn) {
                                if (!parent) return;

                                visitFn(parent);

                                var children = childrenFn(parent);
                                if (children) {
                                    var count = children.length;
                                    for (var i = 0; i < count; i++) {
                                        visit(children[i], visitFn, childrenFn);
                                    }
                                }
                            }

                            // Call visit function to establish maxLabelLength
                            visit(treeData, function(d) {
                                totalNodes++;
                                maxLabelLength = Math.max(d.name.length, maxLabelLength);

                            }, function(d) {
                                return d.children && d.children.length > 0 ? d.children : null;
                            });


                            // sort the tree according to the node names

                            function sortTree() {
                                tree.sort(function(a, b) {
                                    return b.name.toLowerCase() < a.name.toLowerCase() ? 1 : -1;
                                });
                            }
                            // Sort the tree initially incase the JSON isn't in a sorted order.
                            sortTree();

                            // TODO: Pan function, can be better implemented.

                            function pan(domNode, direction) {
                                var speed = panSpeed;
                                if (panTimer) {
                                    clearTimeout(panTimer);
                                    translateCoords = d3.transform(svgGroup.attr("transform"));
                                    if (direction == 'left' || direction == 'right') {
                                        translateX = direction == 'left' ? translateCoords.translate[0] + speed : translateCoords.translate[0] - speed;
                                        translateY = translateCoords.translate[1];
                                    } else if (direction == 'up' || direction == 'down') {
                                        translateX = translateCoords.translate[0];
                                        translateY = direction == 'up' ? translateCoords.translate[1] + speed : translateCoords.translate[1] - speed;
                                    }
                                    scaleX = translateCoords.scale[0];
                                    scaleY = translateCoords.scale[1];
                                    scale = zoomListener.scale();
                                    svgGroup.transition().attr("transform", "translate(" + translateX + "," + translateY + ")scale(" + scale + ")");
                                    d3.select(domNode).select('g.node').attr("transform", "translate(" + translateX + "," + translateY + ")");
                                    zoomListener.scale(zoomListener.scale());
                                    zoomListener.translate([translateX, translateY]);
                                    panTimer = setTimeout(function() {
                                        pan(domNode, speed, direction);
                                    }, 50);
                                }
                            }

                            // Define the zoom function for the zoomable tree

                            function zoom() {
                                svgGroup.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
                            }


                            // define the zoomListener which calls the zoom function on the "zoom" event constrained within the scaleExtents
                            var zoomListener = d3.behavior.zoom().scaleExtent([0.1, 3]).on("zoom", zoom);

                            function initiateDrag(d, domNode) {
                                draggingNode = d;
                                d3.select(domNode).select('.ghostCircle').attr('pointer-events', 'none');
                                d3.selectAll('.ghostCircle').attr('class', 'ghostCircle show');
                                d3.select(domNode).attr('class', 'node activeDrag');

                                svgGroup.selectAll("g.node").sort(function(a, b) { // select the parent and sort the path's
                                    if (a.id != draggingNode.id) return 1; // a is not the hovered element, send "a" to the back
                                    else return -1; // a is the hovered element, bring "a" to the front
                                });
                                // if nodes has children, remove the links and nodes
                                if (nodes.length > 1) {
                                    // remove link paths
                                    links = tree.links(nodes);
                                    nodePaths = svgGroup.selectAll("path.link")
                                        .data(links, function(d) {
                                            return d.target.id;
                                        }).remove();
                                    // remove child nodes
                                    nodesExit = svgGroup.selectAll("g.node")
                                        .data(nodes, function(d) {
                                            return d.id;
                                        }).filter(function(d, i) {
                                            if (d.id == draggingNode.id) {
                                                return false;
                                            }
                                            return true;
                                        }).remove();
                                }

                                // remove parent link
                                parentLink = tree.links(tree.nodes(draggingNode.parent));
                                svgGroup.selectAll('path.link').filter(function(d, i) {
                                    if (d.target.id == draggingNode.id) {
                                        return true;
                                    }
                                    return false;
                                }).remove();

                                dragStarted = null;
                            }

                            // define the baseSvg, attaching a class for styling and the zoomListener
                            var baseSvg = d3.select("#tree-container").append("svg")
                                .attr("width", viewerWidth)
                                .attr("height", viewerHeight)
                                .attr("class", "overlay")
                                .call(zoomListener);


                            // Define the drag listeners for drag/drop behaviour of nodes.
                            dragListener = d3.behavior.drag()
                                .on("dragstart", function(d) {
                                    if (d == root) {
                                        return;
                                    }
                                    dragStarted = true;
                                    nodes = tree.nodes(d);
                                    d3.event.sourceEvent.stopPropagation();
                                    // it's important that we suppress the mouseover event on the node being dragged. Otherwise it will absorb the mouseover event and the underlying node will not detect it d3.select(this).attr('pointer-events', 'none');
                                })
                                .on("drag", function(d) {
                                    if (d == root) {
                                        return;
                                    }
                                    if (dragStarted) {
                                        domNode = this;
                                        initiateDrag(d, domNode);
                                    }

                                    // get coords of mouseEvent relative to svg container to allow for panning
                                    relCoords = d3.mouse($('svg').get(0));
                                    if (relCoords[0] < panBoundary) {
                                        panTimer = true;
                                        pan(this, 'left');
                                    } else if (relCoords[0] > ($('svg').width() - panBoundary)) {

                                        panTimer = true;
                                        pan(this, 'right');
                                    } else if (relCoords[1] < panBoundary) {
                                        panTimer = true;
                                        pan(this, 'up');
                                    } else if (relCoords[1] > ($('svg').height() - panBoundary)) {
                                        panTimer = true;
                                        pan(this, 'down');
                                    } else {
                                        try {
                                            clearTimeout(panTimer);
                                        } catch (e) {

                                        }
                                    }

                                    d.x0 += d3.event.dy;
                                    d.y0 += d3.event.dx;
                                    var node = d3.select(this);
                                    node.attr("transform", "translate(" + d.y0 + "," + d.x0 + ")");
                                    updateTempConnector();
                                }).on("dragend", function(d) {
                                    if (d == root) {
                                        return;
                                    }
                                    domNode = this;
                                    if (selectedNode) {
                                        // now remove the element from the parent, and insert it into the new elements children
                                        var index = draggingNode.parent.children.indexOf(draggingNode);
                                        if (index > -1) {
                                            draggingNode.parent.children.splice(index, 1);
                                        }
                                        if (typeof selectedNode.children !== 'undefined' || typeof selectedNode._children !== 'undefined') {
                                            if (typeof selectedNode.children !== 'undefined') {
                                                selectedNode.children.push(draggingNode);
                                            } else {
                                                selectedNode._children.push(draggingNode);
                                            }
                                        } else {
                                            selectedNode.children = [];
                                            selectedNode.children.push(draggingNode);
                                        }
                                        // Make sure that the node being added to is expanded so user can see added node is correctly moved
                                        expand(selectedNode);
                                        sortTree();
                                        endDrag();
                                    } else {
                                        endDrag();
                                    }
                                });

                            function endDrag() {
                                selectedNode = null;
                                d3.selectAll('.ghostCircle').attr('class', 'ghostCircle');
                                d3.select(domNode).attr('class', 'node');
                                // now restore the mouseover event or we won't be able to drag a 2nd time
                                d3.select(domNode).select('.ghostCircle').attr('pointer-events', '');
                                updateTempConnector();
                                if (draggingNode !== null) {
                                    update(root);
                                    centerNode(draggingNode);
                                    draggingNode = null;
                                }
                            }

                            // Helper functions for collapsing and expanding nodes.

                            function collapse(d) {
                                if (d.children) {
                                    d._children = d.children;
                                    d._children.forEach(collapse);
                                    d.children = null;
                                }
                            }

                            function expand(d) {
                                if (d._children) {
                                    d.children = d._children;
                                    d.children.forEach(expand);
                                    d._children = null;
                                }
                            }

                            var overCircle = function(d) {
                                selectedNode = d;
                                updateTempConnector();
                            };
                            var outCircle = function(d) {
                                selectedNode = null;
                                updateTempConnector();
                            };

                            // Function to update the temporary connector indicating dragging affiliation
                            var updateTempConnector = function() {
                                var data = [];
                                if (draggingNode !== null && selectedNode !== null) {
                                    // have to flip the source coordinates since we did this for the existing connectors on the original tree
                                    data = [{
                                        source: {
                                            x: selectedNode.y0,
                                            y: selectedNode.x0
                                        },
                                        target: {
                                            x: draggingNode.y0,
                                            y: draggingNode.x0
                                        }
                                    }];
                                }
                                var link = svgGroup.selectAll(".templink").data(data);

                                link.enter().append("path")
                                    .attr("class", "templink")
                                    .attr("d", d3.svg.diagonal())
                                    .attr('pointer-events', 'none');

                                link.attr("d", d3.svg.diagonal());

                                link.exit().remove();
                            };

                            // Function to center node when clicked/dropped so node doesn't get lost when collapsing/moving with large amount of children.

                            function centerNode(source) {
                                scale = zoomListener.scale();
                                x = -source.y0;
                                y = -source.x0;
                                x = x * scale + viewerWidth / 4;
                                y = y * scale + viewerHeight / 2;
                                d3.select('g').transition()
                                    .duration(duration)
                                    .attr("transform", "translate(" + x + "," + y + ")scale(" + scale + ")");
                                zoomListener.scale(scale);
                                zoomListener.translate([x, y]);
                            }

                            // Toggle children function

                            function toggleChildren(d) {
                                if (d.children) {
                                    d._children = d.children;
                                    d.children = null;
                                } else if (d._children) {
                                    d.children = d._children;
                                    d._children = null;
                                }
                                return d;
                            }

                            // Toggle children on click.

                            function click(d) {
                                if (d3.event.defaultPrevented) return; // click suppressed
                                d = toggleChildren(d);
                                update(d);
                                centerNode(d);
                            }

                            function update(source) {
                                // Compute the new height, function counts total children of root node and sets tree height accordingly.
                                // This prevents the layout looking squashed when new nodes are made visible or looking sparse when nodes are removed
                                // This makes the layout more consistent.
                                var levelWidth = [1];
                                var childCount = function(level, n) {

                                    if (n.children && n.children.length > 0) {
                                        if (levelWidth.length <= level + 1) levelWidth.push(0);

                                        levelWidth[level + 1] += n.children.length;
                                        n.children.forEach(function(d) {
                                            childCount(level + 1, d);
                                        });
                                    }
                                };
                                childCount(0, root);
                                var newHeight = d3.max(levelWidth) * 30; // 25 pixels per line
                                tree = tree.size([newHeight, viewerWidth]);

                                // Compute the new tree layout.
                                var nodes = tree.nodes(root).reverse(),
                                    links = tree.links(nodes);

                                // Set widths between levels based on maxLabelLength.
                                nodes.forEach(function(d) {
                                    d.y = (d.depth * (maxLabelLength * 3)); //maxLabelLength * 10px
                                    // alternatively to keep a fixed scale one can set a fixed depth per level
                                    // Normalize for fixed-depth by commenting out below line
                                    // d.y = (d.depth * 500); //500px per level.
                                });

                                // Update the nodes…
                                node = svgGroup.selectAll("g.node")
                                    .data(nodes, function(d) {
                                        return d.id || (d.id = ++i);
                                    });

                                // Enter any new nodes at the parent's previous position.
                                var nodeEnter = node.enter().append("g")
                                    .call(dragListener)
                                    .attr("class", "node")
                                    .attr("transform", function(d) {
                                        return "translate(" + source.y0 + "," + source.x0 + ")";
                                    })
                                    .on('click', click);

                                nodeEnter.append("circle")
                                    .attr('class', 'nodeCircle')
                                    .attr("r", 0)
                                    .style("fill", function(d) {
                                        return d._children ? "lightsteelblue" : "#fff";
                                    });

                                nodeEnter.append("text")
                                    .attr("x", function(d) {
                                        return d.children || d._children ? -10 : 10;
                                    })
                                    .attr("dy", ".35em")
                                    .attr('class', 'nodeText')
                                    .attr("text-anchor", function(d) {
                                        return d.children || d._children ? "end" : "start";
                                    })
                                    .text(function(d) {
                                        return d.name;
                                    })
                                    .style("fill-opacity", 0);

                                // phantom node to give us mouseover in a radius around it
                                nodeEnter.append("circle")
                                    .attr('class', 'ghostCircle')
                                    .attr("r", 30)
                                    .attr("opacity", 0.2) // change this to zero to hide the target area
                                    .style("fill", "red")
                                    .attr('pointer-events', 'mouseover')
                                    .on("mouseover", function(node) {
                                        overCircle(node);
                                    })
                                    .on("mouseout", function(node) {
                                        outCircle(node);
                                    });

                                // Update the text to reflect whether node has children or not.
                                node.select('text')
                                    .attr("x", function(d) {
                                        return d.children || d._children ? -10 : 10;
                                    })
                                    .attr("text-anchor", function(d) {
                                        return d.children || d._children ? "end" : "start";
                                    })
                                    .text(function(d) {
                                        return d.name;
                                    });

                                // Change the circle fill depending on whether it has children and is collapsed
                                node.select("circle.nodeCircle")
                                    .attr("r", 4.5)
                                    .style("fill", function(d) {
                                        return d._children ? "lightsteelblue" : "#fff";
                                    });

                                // Transition nodes to their new position.
                                var nodeUpdate = node.transition()
                                    .duration(duration)
                                    .attr("transform", function(d) {
                                        return "translate(" + d.y + "," + d.x + ")";
                                    });

                                // Fade the text in
                                nodeUpdate.select("text")
                                    .style("fill-opacity", 1);

                                // Transition exiting nodes to the parent's new position.
                                var nodeExit = node.exit().transition()
                                    .duration(duration)
                                    .attr("transform", function(d) {
                                        return "translate(" + source.y + "," + source.x + ")";
                                    })
                                    .remove();

                                nodeExit.select("circle")
                                    .attr("r", 0);

                                nodeExit.select("text")
                                    .style("fill-opacity", 0);

                                // Update the links…
                                var link = svgGroup.selectAll("path.link")
                                    .data(links, function(d) {
                                        return d.target.id;
                                    });

                                // Enter any new links at the parent's previous position.
                                link.enter().insert("path", "g")
                                    .attr("class", "link")
                                    .attr("d", function(d) {
                                        var o = {
                                            x: source.x0,
                                            y: source.y0
                                        };
                                        return diagonal({
                                            source: o,
                                            target: o
                                        });
                                    });

                                // Transition links to their new position.
                                link.transition()
                                    .duration(duration)
                                    .attr("d", diagonal);

                                // Transition exiting nodes to the parent's new position.
                                link.exit().transition()
                                    .duration(duration)
                                    .attr("d", function(d) {
                                        var o = {
                                            x: source.x,
                                            y: source.y
                                        };
                                        return diagonal({
                                            source: o,
                                            target: o
                                        });
                                    })
                                    .remove();

                                // Stash the old positions for transition.
                                nodes.forEach(function(d) {
                                    d.x0 = d.x;
                                    d.y0 = d.y;
                                });
                            }

                            // Append a group which holds all nodes and which the zoom Listener can act upon.
                            var svgGroup = baseSvg.append("g");

                            // Define the root
                            root = treeData;
                            root.x0 = viewerHeight / 2;
                            root.y0 = 0;

                            // Layout the tree initially and center on the root node.
                            update(root);
                            centerNode(root);


                        //    *******************************************************************

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


