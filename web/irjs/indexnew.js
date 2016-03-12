$(function() {
	excuteNLPIR();
	
	$("#smt").click(function() {
		var params = {
				"link" : $("#rsslink").val()
			};
			
		$.post("index/getContentByLink.do", params, function(data){
			var jsonlist = $.parseJSON(data);
			var content = jsonlist.content;
			$(".box4 .input").attr("value", content);
		});
	});
	
	$(".anniu .img1").click(function() {
		var content = $.trim($(".box4 .input").val());
		if (content.length < 30) {
			alert("请输入30以上的文字");
			return;
		}
		
		excuteNLPIR();
	});
	
	$(".cximg2").click(function(){
		var params = {
				"type" : "addAndGet", 
				"userWord" : $("#yonghuci").val(), 
				"content" : $.trim($(".box4 .input").val())
			};
		
		$.post("index/getAllContentNew.do", params, function(data){
			var jsonlist = $.parseJSON(data);
			var words = jsonlist.dividewords;
			$("#dividewords").empty();
			$("#dividewords").append(splitString(words, 1));
		});
	});
	
	$("#tb2").click(function() {
		$("#stcq1").show();
		$("#stcq2").hide();
	});
	
	$("#wb2").click(function() {
		$("#stcq1").hide();
		$("#stcq2").show();
	});
	
	$("#tb3").click(function() {
		$("#wordfreq1").show();
		$("#wordfreq2").hide();
	});
	
	$("#wb3").click(function() {
		$("#wordfreq1").hide();
		$("#wordfreq2").show();
	});
	
	$("#tb6").click(function() {
		$("#keyws1").show();
		$("#keyws2").hide();
	});
	
	$("#wb6").click(function() {
		$("#keyws1").hide();
		$("#keyws2").show();
	});
	
	$("#simple").click(function() {
		var content = $("#transform").text();
		$("#transform").empty();
		$("#transform").append(string_prototype_t2s(content));
	});
	
	$("#tradition").click(function() {
		var content = $("#transform").text();
		$("#transform").empty();
		$("#transform").append(string_prototype_s2t(content));
	});
});

function excuteNLPIR() {
	var type = "all";
	var content = $.trim($(".box4 .input").val());
	
	if (content.length > 50000) {
		alert("输入文字的上限为50000，请不要超过该上限");
		return;
	}
	
	var params = {
			"type" : type, 
			"content" : content
		};
	
	$.post("index/getAllContentNew.do", params, function(data){
		var jsonlist = $.parseJSON(data);
		
		// 1.词性分析
		var words = jsonlist.dividewords;
		$("#dividewords").empty();
		$("#dividewords").append(splitString(words, 1));
		
		var newwords = jsonlist.newwords;
		$("#newws").empty();
		$("#newws").append(setNewWS(newwords));
		
		// 2.实体抽取
		
		// 控制两个div的显示
		$("#stcq1").show();
		$("#stcq2").hide();
		
		// 第一个div的内容填充
		var doc = jsonlist.doclist;
		var docArr = new Array();
		docArr = setDoc(doc);
		
		// 第二个div的内容填充
		// 人名
		$("#persons").empty();
		$("#persons").append(splitString(doc[0], 2));
		
		// 地名
		$("#locations").empty();
		$("#locations").append(splitString(doc[1], 2));
		
		// 机构名
		$("#organizations").empty();
		$("#organizations").append(splitString(doc[2], 2));
		
		// 关键词
		$("#keywords").empty();
		$("#keywords").append(splitString(doc[3], 2));
		
		// 作者
		$("#authors").empty();
		$("#authors").append(splitString(doc[4], 2));
		
		// 媒体
		$("#medias").empty();
		$("#medias").append(splitString(doc[5], 2));
		
		// 3.词频统计
		
		// 控制两个div的显示
		$("#wordfreq1").show();
		$("#wordfreq2").hide();
		
		// 第一个div的内容填充
		var freq = jsonlist.wordFreq;
		var wordfreq = new Array();
		wordfreq = setFreq(freq);
		
		// 第二个div的内容填充
		$("#wordfreq2").empty();
		$("#wordfreq2").append(splitString(freq, 3));
		
		// 4.文本分类
		var classifierName = jsonlist.classifierName;
		var cfn = 93.75;
		if(classifierName == '交通'){
			cfn = 6.25;
		}else if(classifierName == '教育'){
			cfn = 18.75;
		}else if(classifierName == '经济'){
			cfn = 31.25;
		}else if(classifierName == '军事'){
			cfn = 43.75;
		}else if(classifierName == '体育'){
			cfn = 56.25;
		}else if(classifierName == '艺术'){
			cfn = 68.75;
		}else if(classifierName == '政治'){
			cfn = 81.25;
		}
		
		// 5.情感分析
		
		// 文本分析
		var stnResult = jsonlist.stnResult;
		var json0 = $.parseJSON(stnResult).json0;
		var positivepoint = json0.positivepoint;//文本正面得分
		var negativepoint = Math.abs(json0.negativepoint);//文本负面得分
		
		// 人物
		var person = jsonlist.person;
		
		// 特定人物分析
		var pStnResult = jsonlist.pStnResult;
		var pJson0 = $.parseJSON(pStnResult).json0;
		var pPositivepoint = pJson0.positivepoint;//特定人物正面得分
		var pNegativepoint = Math.abs(pJson0.negativepoint);//特定人物负面得分
		
		// 6.关键词提取
		
		// 控制两个div的显示
		$("#keyws1").show();
		$("#keyws2").hide();
		
		var keywords = jsonlist.keywords;
		$("#keyws2").empty();
		
		// 词云图
		var color = d3.scale.category20();
		d3.layout.cloud().size([920, 332])
		.words(setWords(keywords).map(function(d) {
			return {text: d, size: 1 * (10 + Math.random() * 50)};//大小差异多少倍
		}))
		.padding(5).rotate(function() { return ~~(Math.random() * 2) * 90; })//90是倾斜角度
		.font("Gorditas").fontSize(function(d) { return d.size; }).on("end", draw).start();
		
		function draw(words) {
			d3.select("#keyws2").append("svg")
			.attr("width", 920).attr("height", 332)
			.append("g").attr("transform", "translate(460,166)")
			.selectAll("text").data(words).enter().append("text").style("font-size", 
			function(d) { return d.size + "px"; })
			.style("font-family", "Gorditas").style("fill", function(d, i) { return color(i); })
			.attr("text-anchor", "middle").attr("transform", function(d) {
				return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
			}).text(function(d) { return d.text; });
		}
		
		// 词云球
		var tagc = setTags(keywords);
		
		var xiSwfUrlStr = "./js/flash/flex/playerProductInstall.swf";//默认播放器位置
		var flashvars = {};
		flashvars.mode = "tags";
		flashvars.tagcloud = tagc;
		flashvars.tcolor = "0xEC695A";//字体颜色，按大小不同设置
		flashvars.tcolor2 = "0x3366CC";
		flashvars.hicolor = "0xEC695A";
		flashvars.tspeed = "150";//tag移动速度
		flashvars.distr = "true";
		
		var params = {};//参数如下设置即可
		params.allowscriptaccess = "always";
		params.quality = "high";
		params.wmode = "transparent";
		
		var attributesa = {};
		attributesa.id = "tagcloud_f";//id名，与flash源名称一致即可
		attributesa.name = "tagcloud_f";
	
		// 嵌入flash的代码，“flash源位置”，“嵌入div的ID”，“宽”，“高”，“flash版本”，“默认播放器位置”，“内容”，“参数”，“id名”
		$("#keyws1").empty();
		$("#keyws1").append('<div id="cloud"></div>');
		swfobject.embedSWF("./js/flash/flex/tagcloud_f.swf", "cloud", "920", "330", "9", xiSwfUrlStr, flashvars, params, attributesa);
		
		// 7.word2vec
		var w2vlist = jsonlist.w2vlist;
		var vjson = jsonlist.vjson;
		var index = jsonlist.index;//获取word2vec有效关键词的索引
		var w2vkey = setWords(keywords)[index] + "\n(关键词)";
		var w2vArr = setW2v(w2vlist, vjson, w2vkey);
		
		// 8.依存句法
		var depend = jsonlist.depend;
		setDepend(depend);
		
		// 9.繁简转换
		$("#transform").empty();
		$("#transform").append(doc[11]);
		
		// 10.自动注音
		$("#spell").empty();
		$("#spell").append($("#transform").toPinyin());
		
		// 11.摘要提取
		var summary = jsonlist.summarylist;
		$("#summary").empty();
		$("#summary").append(summary[0]);
		
		content = jsonlist.content;
		var rsslink = jsonlist.rsslink;
		
		$(".box4 .input").attr("value", content);
		$("#rsslink").attr("value", rsslink);
		
		// 路径配置
		require.config({
			paths : {
				echarts : './build/dist'
			}
		});
		
		// 使用
		require(
			[
				'echarts',
				'echarts/chart/force',
				'echarts/chart/chord',
				'echarts/chart/bar',
				'echarts/chart/line',
				'echarts/chart/gauge',
				'echarts/chart/pie',
				'echarts/chart/funnel'
            ],
			function (ec) {
				// 基于准备好的dom，初始化echarts图表
				var forceChart2 = ec.init(document.getElementById('main2'));
				var barChartA = ec.init(document.getElementById('main3a'));
				var barChartB = ec.init(document.getElementById('main3b'));
				var barChartC = ec.init(document.getElementById('main3c'));
				var gaugeChart = ec.init(document.getElementById('main4'));
				var pieChartA = ec.init(document.getElementById('main5a'));
				var pieChartB = ec.init(document.getElementById('main5b'));
				var forceChart7 = ec.init(document.getElementById('main7'));
				
				var option2 = {
					title : {
						text : '',
						subtext : '',
						x : 'right',
						y : 'bottom'
					},
					tooltip : {
						trigger : 'item',
						formatter : '{a} : {b}'
					},
					toolbox : {
						show : true,
						feature : {
							restore : { show : true },
							magicType : { show : true, type : ['force', 'chord'] },
							saveAsImage : { show : true }
						}
					},
					legend : {
						x : 'left',
						data : ['实体类型', '实体内容']
					},
					series : [{
						type : 'force',
						name : "内容",
						ribbonType : false,
						categories : [
							{
								name : '文本'
							},
							{
								name : '实体类型'
							},
							{
								name : '实体内容'
							}
						],
						itemStyle : {
							normal : {
								label : {
									show : true,
									textStyle : {
										color : '#333'
									}
								},
								nodeStyle : {
									brushType : 'both',
									borderColor : 'rgba(255,215,0,0.4)',
									borderWidth : 1
								},
								linkStyle : {
									type : 'curve'
								}
							},
							emphasis : {
								label : { show : false },
								nodeStyle : {},
								linkStyle : {}
							}
						},
						useWorker : false,
						minRadius : 15,
						maxRadius : 25,
						gravity : 1.1,
						scaling : 1.1,
						roam : 'move',
						nodes : docArr[0],
						links : docArr[1]
					}]
				};
				
				var option3a = {
					title : {
						text : '       名词',
						subtext : ''
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : ['']
					},
					toolbox : {
						show : true,
						feature : {
							mark : { show : true },
							dataView : { show : true, readOnly : false },
							magicType : { show : true, type : ['line', 'bar'] },
							restore : { show : true },
							saveAsImage : { show : true }
						}
					},
					calculable : true,
					xAxis : [{
						type : 'value'
					}],
					yAxis : [{
						type : 'category',
						data : wordfreq[0]
					}],
					series : [{
						name : '词频统计',
						type : 'bar',
						itemStyle : {
							normal : { color : "#ff7f50" }
						},
						data : wordfreq[1]
					}]
				};
				
				var option3b = {
					title : {
						text : '       动词',
						subtext : ''
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : ['']
					},
					toolbox : {
						show : true,
						feature : {
							mark : { show : true },
							dataView : { show : true, readOnly : false },
							magicType : { show : true, type : ['line', 'bar'] },
							restore : { show : true },
							saveAsImage : { show : true }
						}
					},
					calculable : true,
					xAxis : [{
						type : 'value'
					}],
					yAxis : [{
						type : 'category',
						data : wordfreq[2]
					}],
					series : [{
						name : '词频统计',
						type : 'bar',
						itemStyle : {
							normal : { color : "#87cefa" }
						},
						data : wordfreq[3]
					}]
				};
				
				var option3c = {
					title : {
						text : '     形容词',
						subtext : ''
					},
					tooltip : {
						trigger : 'axis'
					},
					legend : {
						data : ['']
					},
					toolbox : {
						show : true,
						feature : {
							mark : { show : true },
							dataView : { show : true, readOnly : false },
							magicType : { show : true, type : ['line', 'bar'] },
							restore : { show : true },
							saveAsImage : { show : true }
						}
					},
					calculable : true,
					xAxis : [{
						type : 'value'
					}],
					yAxis : [{
						type : 'category',
						data : wordfreq[4]
					}],
					series : [{
						name : '词频统计',
						type : 'bar',
						itemStyle : {
							normal : { color : "#da70d6" }
						},
						data : wordfreq[5]
					}]
				};
				
				var option4 = {
					tooltip : {
						formatter : "{a} <br/>{b} : {c}%"
					},
					toolbox : {
						show : true,
						feature : {
							mark : { show : true },
							restore : { show : true },
							saveAsImage : { show : true }
						}
					},
					series : [{
						name : '文本分类',
						type : 'gauge',
						center : ['50%', '50%'],
						radius : [0, '75%'],
						startAngle : 140,
						endAngle : -140,
						min : 0,
						max : 100,
						precision : 0,
						splitNumber : 16,
						axisLine : {
							show : true,
							lineStyle : {
								color : [
									[0.125, '#99ff99'],
									[0.25, '#ffbb66'],
									[0.375, '#33ccff'],
									[0.5, '#888800'],
									[0.625, '#ffff33'],
									[0.75, '#cc00ff'],
									[0.875, '#ff5511'],
									[1, '#444444']
								],
								width : 30
							}
						},
						axisTick : {
							show : true,
							splitNumber : 5,
							length : 8,
							lineStyle : {
								color : '#eee',
								width : 1,
								type : 'solid'
							}
						},
						axisLabel : {
							show : true,
							formatter : function(v) {
								switch (v + '') {
									case '6.25' : return '交通';
									case '18.75' : return '教育';
									case '31.25' : return '经济';
									case '43.75' : return '军事';
									case '56.25' : return '体育';
									case '68.75' : return '艺术';
									case '81.25' : return '政治';
									case '93.75' : return '其他';
									default : return '';
								}
							},
							textStyle : {
								color : '#333'
							}
						},
						splitLine : {
							show : true,
							length : 30,
							lineStyle : {
								color : '#eee',
								width : 2,
								type : 'solid'
							}
						},
						pointer : {
							length : '80%',
							width : 8,
							color : 'auto'
						},
						title : {
							show : true,
							offsetCenter : ['-65%', -10],
							textStyle : {
								color : '#333',
								fontSize : 12
							}
						},
						detail : {
							show : true,
							backgroundColor : 'rgba(0,0,0,0)',
							borderWidth : 0,
							borderColor : '#ccc',
							width : 100,
							height : 40,
							offsetCenter : ['-60%', 10],
							formatter : classifierName,
							textStyle : {
								color : 'auto',
								fontSize : 24
							}
						},
						data : [{
							value : cfn,
							name : '类别'
						}]
					}]
				};
				
				var option5a = {
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b} : {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						x : 'left',
						data : ['文章正面得分','文章负面得分']
					},
					toolbox : {
						show : true,
						feature : {
							mark : { show : true },
							dataView : { show : true, readOnly : false },
							magicType : {
								show : true, 
								type : ['pie', 'funnel'],
								option : {
									funnel : {
										x : '25%',
										width : '50%',
										funnelAlign : 'center',
										max : 15
									}
								}
							},
							restore : { show : true },
							saveAsImage : { show : true }
						}
					},
					calculable : true,
					series : [{
						name : '文章得分',
						type : 'pie',
						radius : ['50%', '70%'],
						itemStyle : {
							normal : {
								label : { show : false },
								labelLine : { show : false }
							},
							emphasis : {
								label : {
									show : true,
									position : 'center',
									textStyle : {
										fontSize : '20',
										fontWeight : 'bold'
									}
								}
							}
						},
						data : [
							{ value : positivepoint, name : '文章正面得分' },
							{ value : negativepoint, name : '文章负面得分' }
						]
					}]
				};
				
				var option5b = {
					tooltip : {
						trigger : 'item',
						formatter : "{a} <br/>{b} : {c} ({d}%)"
					},
					legend : {
						orient : 'vertical',
						x : 'left',
						data : ['特定人物正面得分','特定人物负面得分']
					},
					toolbox : {
						show : true,
						feature : {
							mark : { show : true },
							dataView : { show : true, readOnly : false },
							magicType : {
								show : true, 
								type : ['pie', 'funnel'],
								option : {
									funnel : {
										x : '25%',
										width : '50%',
										funnelAlign : 'center',
										max : 15
									}
								}
							},
							restore : { show : true },
							saveAsImage : { show : true }
						}
					},
					calculable : true,
					series : [{
						name : person + ' 得分',
						type : 'pie',
						radius : ['50%', '70%'],
						itemStyle : {
							normal : {
								label : { show : false },
								labelLine : { show : false }
							},
							emphasis : {
								label : {
									show : true,
									position : 'center',
									textStyle : {
										fontSize : '20',
										fontWeight : 'bold'
									}
								}
							}
						},
						data : [
							{ value : pPositivepoint, name : '特定人物正面得分' },
							{ value : pNegativepoint, name : '特定人物负面得分' }
						]
					}]
				};
				
				var option7 = {
					title : {
						text : '',
						subtext : '',
						x : 'right',
						y : 'bottom'
					},
					tooltip : {
						trigger : 'item',
						formatter : '{a} : {b}'
					},
					toolbox : {
						show : true,
						feature : {
							restore : { show : true },
							magicType : { show : true, type : ['force', 'chord'] },
							saveAsImage : { show : true }
						}
					},
					legend : {
						x : 'left',
						data : ['相关词', '相关词的相关词']
					},
					series : [{
						type : 'force',
						name : "相关性",
						ribbonType : false,
						categories : [
							{
								name : '关键词'
							},
							{
								name : '相关词'
							},
							{
								name : '相关词的相关词'
							}
						],
						itemStyle : {
							normal : {
								label : {
									show : true,
									textStyle : {
										color : '#333'
									}
								},
								nodeStyle : {
									brushType : 'both',
									borderColor : 'rgba(255,215,0,0.4)',
									borderWidth : 1
								},
								linkStyle : {
									type : 'curve'
								}
							},
							emphasis : {
								label : { show : false },
								nodeStyle : {},
								linkStyle : {}
							}
						},
						useWorker : false,
						minRadius : 15,
						maxRadius : 25,
						gravity : 1.1,
						scaling : 1.1,
						roam : 'move',
						nodes : w2vArr[0],
						links : w2vArr[1]
					}]
				};
				
				// 为echarts对象加载数据
				forceChart2.setOption(option2);
				barChartA.setOption(option3a);
				barChartB.setOption(option3b);
				barChartC.setOption(option3c);
				gaugeChart.setOption(option4);
				pieChartA.setOption(option5a);
				pieChartB.setOption(option5b);
				forceChart7.setOption(option7);
			}
		);
	});
}

function splitString(str, num) {
	var arr = new Array();
	
	if (num == 1) {
		arr = str.toString().split(" ");
	} else if (num == 2 || num ==3) {
		arr = str.toString().split("#");
	}
	
	var divModel = '';
	for (var i = 0; i < arr.length - 1; i++) {
		var word = arr[i];
		if (word != '') {
			var cla = "";
			if (num == 1) {
				cla = addStyle(arr[i]);
				// 处理英文复合词
				if(word.indexOf("+") > 0){
					var wordArr = word.toString().split("/");
					var word0 = "[" + wordArr[0].replace(new RegExp("\\+", "g"), " ") + "]";
					word = word0 + "/" + wordArr[1];
				}
			} else if (num ==3) {
				cla = addStyle(arr[i]);
			} else {
				cla = "nn";//实体抽取的样式
			}
			divModel += "<div class = '" + cla + "'>" + word + "</div>";
		}
	}
	
	return divModel;
}

function setNewWS(str){
	 var arr = str.toString().split("#");
	 var newws = "";
	 
	 for(var i = 0; i < arr.length; i++){
	 	if(arr[i] != ""){
	 		newws += "<div class = 'n23'>" + arr[i] + "</div>";
	 	}
	 }
	 
	 return newws;
}

function setDoc(doc){
	var nodesArr = new Array();
	var linksArr = new Array();
	var resultArr = new Array();
	
	nodesArr.push({category : 0, name : '文本', value : 6, lable : '文本\n(ROOT)'});
	nodesArr.push({category : 1, name : '人名', value : 5});
	nodesArr.push({category : 1, name : '地名', value : 5});
	nodesArr.push({category : 1, name : '机构名', value : 5});
	nodesArr.push({category : 1, name : '关键词', value : 5});
	nodesArr.push({category : 1, name : '作者', value : 5});
	nodesArr.push({category : 1, name : '媒体', value : 5});
	
	linksArr.push({source : '人名', target : '文本', weight : 1, name : '实体'});
	linksArr.push({source : '地名', target : '文本', weight : 1, name : '实体'});
	linksArr.push({source : '机构名', target : '文本', weight : 1, name : '实体'});
	linksArr.push({source : '关键词', target : '文本', weight : 1, name : '实体'});
	linksArr.push({source : '作者', target : '文本', weight : 1, name : '实体'});
	linksArr.push({source : '媒体', target : '文本', weight : 1, name : '实体'});
	
	for(var i = 0; i < 6; i++){
		var docArr = doc[i].toString().split("#");
		
		var stType = '人名';
		if(i == 1){
			stType = '地名';
		}else if(i == 2){
			stType = '机构名';
		}else if(i == 3){
			stType = '关键词';
		}else if(i == 4){
			stType = '作者';
		}else if(i == 5){
			stType = '媒体';
		}
		
		for(var j = 0; j < docArr.length; j++){
			if(docArr[j] != ''){
				nodesArr.push({category : 2, name : docArr[j], value : 5});
				linksArr.push({source : docArr[j], target : stType, weight : 1, name: '内容'});
			}
		}
	}
	
	resultArr.push(nodesArr);
	resultArr.push(linksArr);
	return resultArr;
}

function setFreq(str){
	var arr1 = new Array();
	var arr2 = new Array();
	var arr3 = new Array();
	var arr4 = new Array();
	var arr5 = new Array();
	var arr6 = new Array();
	var arr7 = new Array();
	var arr8 = new Array();
	
	arr1 = str.toString().split("#");
	for (var i = 0; i < arr1.length; i++) {
		var fName = arr1[i].toString().split("/")[0];
		var fNum = arr1[i].toString().split("/")[2];
		
		if(fName.length > 1){
			if(arr1[i].indexOf("/n") > 0){
				if(arr2.length < 10){
					arr2.push(fName);
					arr3.push(fNum);
				}
			}else if(arr1[i].indexOf("/v") > 0){
				if(arr4.length < 10){
					arr4.push(fName);
					arr5.push(fNum);
				}
			}else if(arr1[i].indexOf("/a") > 0){
				if(arr6.length < 10){
					arr6.push(fName);
					arr7.push(fNum);
				}
			}
		}
		
	}
	
	arr8.push(arr2);
	arr8.push(arr3);
	arr8.push(arr4);
	arr8.push(arr5);
	arr8.push(arr6);
	arr8.push(arr7);
	return arr8;
}

function setWords(str){
	var arr1 = new Array();
	var arr2 = new Array();
	
	arr1 = str.toString().split("#");
	for (var i = 0; i < arr1.length; i++) {
		if (arr1[i] != '') {
			arr2.push(arr1[i]);
		}
	}
	
	return arr2;
}

function setTags(str){
	var arr = new Array();
	var kwTags = "<tags>";
	
	arr = str.toString().split("#");
	var len = arr.length;
	if(len > 20){
		len = 20;
	}
	
	for(var i = 0; i < len; i++){
		var styNum = Math.round((Math.random() + 1) * 10);//样式编号，决定颜色和字体的大小
		kwTags += "<a href='javascript:void(0);' style='" + styNum + "'>" + arr[i] + "</a>";
	}
	
	kwTags += "</tags>";
	return kwTags;
}

function setDepend(depend){
	var words = new Array();
	var positions = new Array();
	var relations = new Array();
	
	words = depend.words.toString().split(",");
	positions = depend.positions.toString().split(",");
	relations = depend.relations.toString().split(",");
	
	var num = words.length;
	var wPoint = 10;
	var hPoint = 250;
	var wid = 80;
	var hei = 50;
	var top0 = 22;
	var top1 = 42;
	var xspa = 8;
	var yspa = hPoint/positions.length;
	
	var c = document.getElementById("myCanvas");
	var ctx = c.getContext("2d");
	c.setAttribute("width", wPoint + (wPoint + wid) * num);
	
	for(var i = 0; i < num; i++){
		// 绘制单词框
		ctx.strokeStyle = "#aaa";
		ctx.strokeRect(wPoint + (wPoint + wid) * i, hPoint, wid, hei);
		
		// 绘制单词
		ctx.font = "Bold 16px Microsoft YaHei";
		ctx.textAlign = "center";
		
		ctx.fillStyle = "#000";
		ctx.fillText(words[i], wPoint + wid/2 + (wPoint + wid) * i, hPoint + top0);
		
		// 绘制下标
		ctx.fillStyle = "#aaa";
		ctx.fillText(i, wPoint + wid/2 + (wPoint + wid) * i, hPoint + top1);
		
		if(i > 0){
			// 绘制关系线
			ctx.strokeStyle = "#aaa";
			ctx.moveTo(wPoint + wid/2 + (wPoint + wid) * i, hPoint);
			ctx.lineTo(wPoint + wid/2 + (wPoint + wid) * i, hPoint - Math.abs(i - positions[i]) * yspa);
			if(i > positions[i]){
				ctx.lineTo(wPoint + wid/2 + (wPoint + wid) * positions[i] + xspa, hPoint - Math.abs(i - positions[i]) * yspa);
				ctx.lineTo(wPoint + wid/2 + (wPoint + wid) * positions[i] + xspa, hPoint - 5);
			}else{
				ctx.lineTo(wPoint + wid/2 + (wPoint + wid) * positions[i] - xspa, hPoint - Math.abs(i - positions[i]) * yspa);
				ctx.lineTo(wPoint + wid/2 + (wPoint + wid) * positions[i] - xspa, hPoint - 5);
			}
			ctx.stroke();
			
			// 绘制箭头
			ctx.beginPath();
			if(i > positions[i]){
				ctx.moveTo(wPoint + wid/2 + (wPoint + wid) * positions[i] + xspa, hPoint);
				ctx.lineTo(wPoint + wid/2 + (wPoint + wid) * positions[i] + xspa + 4, hPoint - 5);
				ctx.lineTo(wPoint + wid/2 + (wPoint + wid) * positions[i] + xspa - 4, hPoint - 5);
			}else{
				ctx.moveTo(wPoint + wid/2 + (wPoint + wid) * positions[i] - xspa, hPoint);
				ctx.lineTo(wPoint + wid/2 + (wPoint + wid) * positions[i] - xspa - 4, hPoint - 5);
				ctx.lineTo(wPoint + wid/2 + (wPoint + wid) * positions[i] - xspa + 4, hPoint - 5);
			}
			ctx.closePath();
			ctx.fill();
			
			// 绘制关系词
			ctx.font = "Bold 10px Microsoft YaHei";
			ctx.textAlign = "center";
			
			ctx.fillStyle = "red";
			if(i > positions[i]){
				// positions[i]为字符串，乘以一个数字变为数字
				ctx.fillText(relations[i], wPoint + wid/2 + (wPoint + wid) * (positions[i] * 1 + (i - positions[i])/2), hPoint - Math.abs(i - positions[i]) * yspa + 5);
			}else{
				ctx.fillText(relations[i], wPoint + wid/2 + (wPoint + wid) * (i + (positions[i] - i)/2), hPoint - Math.abs(i - positions[i]) * yspa + 5);
			}
		}
		
	}
	
}

function setW2v(list, vjson, w2vkey){
	var arr1 = new Array();
	var arr3 = new Array();
	var arr4 = new Array();
	var arr5 = new Array();
	
	arr3.push({category : 0, name : w2vkey, value : 1, label : w2vkey});
	
	arr1 = list.toString().split(",");
	for(var i = 0; i < arr1.length; i = i + 2){
		arr3.push({category : 1, name : arr1[i], value : arr1[i+1]});
		arr4.push({source : arr1[i], target : w2vkey, weight : arr1[i+1], name : arr1[i+1]});
	}
	
	var varr0 = vjson.v0.toString().split(",");
	if(varr0 != null && varr0 != ''){
		for(var j = 0; j < varr0.length; j = j + 2){
			arr3.push({category : 2, name : varr0[j], value : varr0[j+1]});
			arr4.push({source : varr0[j], target : arr1[0], weight : varr0[j+1], name : varr0[j+1]});
		}
	}
	
	var varr1 = vjson.v1.toString().split(",");
	if(varr1 != null && varr1 != ''){
		for(var j = 0; j < varr1.length; j = j + 2){
			arr3.push({category : 2, name : varr1[j], value : varr1[j+1]});
			arr4.push({source : varr1[j], target : arr1[2], weight : varr1[j+1], name : varr1[j+1]});
		}
	}
	
	var varr2 = vjson.v2.toString().split(",");
	if(varr2 != null && varr2 != ''){
		for(var j = 0; j < varr2.length; j = j + 2){
			arr3.push({category : 2, name : varr2[j], value : varr2[j+1]});
			arr4.push({source : varr2[j], target : arr1[4], weight : varr2[j+1], name : varr2[j+1]});
		}
	}
	
	arr5.push(arr3);
	arr5.push(arr4);
	return arr5;
}

function addStyle(str){
	var nArr = ["n", "nr", "nr1", "nr2", "nrj", "nrf", "ns", "nsf", "nt", "nz", "nl", "ng"];
	var vArr = ["v", "vd", "vn", "vshi", "vyou", "vf", "vx", "vi", "vl", "vg"];
	var aArr = ["a", "ad", "an", "ag", "al"];
	var tArr = ["t", "tg"];
	var fArr = ["f"];
	var mArr = ["m", "mq"];
	var rArr = ["r", "rr", "rz", "rzt", "rzs", "rzv", "ry", "ryt", "rys", "ryv"];
	var sArr = ["s"];
	var bArr = ["b", "bl"];
	var zArr = ["z"];
	var qArr = ["q", "qv", "qt"];
	var dArr = ["d"];
	var yArr = ["y"];
	var oArr = ["o"];
	var xArr = ["x", "xe", "xs", "xm", "xu"];
	var pArr = ["p", "pba", "pbei"];
	var cArr = ["c", "cc"];
	var uArr = ["u", "uzhe", "ule", "uguo", "ude1", "ude2", "ude3", "usuo", "udeng", "uyy", "udh", "uls", "uzhi", "ulian"];
	var eArr = ["e"];
	var wArr = ["w", "wkz", "wky", "wyz", "wyy", "wj", "ww", "wt", "wd", "wf", "wn", "wm", "ws", "wp", "wb", "wh"];
	var hArr = ["h"];
	var kArr = ["k"];
	
	var AllArr = new Array();
	AllArr.push(nArr);
	AllArr.push(vArr);
	AllArr.push(aArr);
	AllArr.push(tArr);
	AllArr.push(fArr);
	AllArr.push(mArr);
	AllArr.push(rArr);
	AllArr.push(sArr);
	AllArr.push(bArr);
	AllArr.push(zArr);
	AllArr.push(qArr);
	AllArr.push(dArr);
	AllArr.push(yArr);
	AllArr.push(oArr);
	AllArr.push(xArr);
	AllArr.push(pArr);
	AllArr.push(cArr);
	AllArr.push(uArr);
	AllArr.push(eArr);
	AllArr.push(wArr);
	AllArr.push(hArr);
	AllArr.push(kArr);
	
	var strArr = str.toString().split("/");
	str = strArr[1].toLowerCase();
	
	var cla = "n22";
	for(var i = 0; i < AllArr.length; i++){
		if($.inArray(str, AllArr[i]) > -1){
			cla = "n" + i;
			break;
		}
	}
	
	return cla;
}

var s = "万与丑专业丛东丝丢两严丧个丬丰临为丽举么义乌乐乔习乡书买乱争于亏云亘亚产亩亲亵亸亿仅从仑仓仪们价众优伙会伛伞伟传伤伥伦伧伪伫体余佣佥侠侣侥侦侧侨侩" +
		"侪侬俣俦俨俩俪俭债倾偬偻偾偿傥傧储傩儿兑兖党兰关兴兹养兽冁内冈册写军农冢冯冲决况冻净凄凉凌减凑凛几凤凫凭凯击凼凿刍划刘则刚创删别刬刭刽刿剀剂剐剑剥" +
		"剧劝办务劢动励劲劳势勋勐勚匀匦匮区医华协单卖卢卤卧卫却卺厂厅历厉压厌厍厕厢厣厦厨厩厮县参叆叇双发变叙叠叶号叹叽吁后吓吕吗吣吨听启吴呒呓呕呖呗员呙呛" +
		"呜咏咔咙咛咝咤咴咸哌响哑哒哓哔哕哗哙哜哝哟唛唝唠唡唢唣唤唿啧啬啭啮啰啴啸喷喽喾嗫呵嗳嘘嘤嘱噜噼嚣嚯团园囱围囵国图圆圣圹场坂坏块坚坛坜坝坞坟坠垄垅垆" +
		"垒垦垧垩垫垭垯垱垲垴埘埙埚埝埯堑堕塆墙壮声壳壶壸处备复够头夸夹夺奁奂奋奖奥妆妇妈妩妪妫姗姜娄娅娆娇娈娱娲娴婳婴婵婶媪嫒嫔嫱嬷孙学孪宁宝实宠审宪宫宽" +
		"宾寝对寻导寿将尔尘尧尴尸尽层屃屉届属屡屦屿岁岂岖岗岘岙岚岛岭岳岽岿峃峄峡峣峤峥峦崂崃崄崭嵘嵚嵛嵝嵴巅巩巯币帅师帏帐帘帜带帧帮帱帻帼幂幞干并广庄庆庐" +
		"庑库应庙庞废庼廪开异弃张弥弪弯弹强归当录彟彦彻径徕御忆忏忧忾怀态怂怃怄怅怆怜总怼怿恋恳恶恸恹恺恻恼恽悦悫悬悭悯惊惧惨惩惫惬惭惮惯愍愠愤愦愿慑慭憷懑" +
		"懒懔戆戋戏戗战戬户扎扑扦执扩扪扫扬扰抚抛抟抠抡抢护报担拟拢拣拥拦拧拨择挂挚挛挜挝挞挟挠挡挢挣挤挥挦捞损捡换捣据捻掳掴掷掸掺掼揸揽揿搀搁搂搅携摄摅摆" +
		"摇摈摊撄撑撵撷撸撺擞攒敌敛数斋斓斗斩断无旧时旷旸昙昼昽显晋晒晓晔晕晖暂暧札术朴机杀杂权条来杨杩杰极构枞枢枣枥枧枨枪枫枭柜柠柽栀栅标栈栉栊栋栌栎栏树" +
		"栖样栾桊桠桡桢档桤桥桦桧桨桩梦梼梾检棂椁椟椠椤椭楼榄榇榈榉槚槛槟槠横樯樱橥橱橹橼檐檩欢欤欧歼殁殇残殒殓殚殡殴毁毂毕毙毡毵氇气氢氩氲汇汉污汤汹沓沟没" +
		"沣沤沥沦沧沨沩沪沵泞泪泶泷泸泺泻泼泽泾洁洒洼浃浅浆浇浈浉浊测浍济浏浐浑浒浓浔浕涂涌涛涝涞涟涠涡涢涣涤润涧涨涩淀渊渌渍渎渐渑渔渖渗温游湾湿溃溅溆溇滗" +
		"滚滞滟滠满滢滤滥滦滨滩滪漤潆潇潋潍潜潴澜濑濒灏灭灯灵灾灿炀炉炖炜炝点炼炽烁烂烃烛烟烦烧烨烩烫烬热焕焖焘煅煳熘爱爷牍牦牵牺犊犟状犷犸犹狈狍狝狞独狭狮" +
		"狯狰狱狲猃猎猕猡猪猫猬献獭玑玙玚玛玮环现玱玺珉珏珐珑珰珲琎琏琐琼瑶瑷璇璎瓒瓮瓯电画畅畲畴疖疗疟疠疡疬疮疯疱疴痈痉痒痖痨痪痫痴瘅瘆瘗瘘瘪瘫瘾瘿癞癣癫" +
		"癯皑皱皲盏盐监盖盗盘眍眦眬着睁睐睑瞒瞩矫矶矾矿砀码砖砗砚砜砺砻砾础硁硅硕硖硗硙硚确硷碍碛碜碱碹磙礼祎祢祯祷祸禀禄禅离秃秆种积称秽秾稆税稣稳穑穷窃窍" +
		"窑窜窝窥窦窭竖竞笃笋笔笕笺笼笾筑筚筛筜筝筹签简箓箦箧箨箩箪箫篑篓篮篱簖籁籴类籼粜粝粤粪粮糁糇紧絷纟纠纡红纣纤纥约级纨纩纪纫纬纭纮纯纰纱纲纳纴纵纶纷" +
		"纸纹纺纻纼纽纾线绀绁绂练组绅细织终绉绊绋绌绍绎经绐绑绒结绔绕绖绗绘给绚绛络绝绞统绠绡绢绣绤绥绦继绨绩绪绫绬续绮绯绰绱绲绳维绵绶绷绸绹绺绻综绽绾绿缀" +
		"缁缂缃缄缅缆缇缈缉缊缋缌缍缎缏缐缑缒缓缔缕编缗缘缙缚缛缜缝缞缟缠缡缢缣缤缥缦缧缨缩缪缫缬缭缮缯缰缱缲缳缴缵罂网罗罚罢罴羁羟羡翘翙翚耢耧耸耻聂聋职聍" +
		"联聩聪肃肠肤肷肾肿胀胁胆胜胧胨胪胫胶脉脍脏脐脑脓脔脚脱脶脸腊腌腘腭腻腼腽腾膑臜舆舣舰舱舻艰艳艹艺节芈芗芜芦苁苇苈苋苌苍苎苏苘苹茎茏茑茔茕茧荆荐荙荚" +
		"荛荜荞荟荠荡荣荤荥荦荧荨荩荪荫荬荭荮药莅莜莱莲莳莴莶获莸莹莺莼萚萝萤营萦萧萨葱蒇蒉蒋蒌蓝蓟蓠蓣蓥蓦蔷蔹蔺蔼蕲蕴薮藁藓虏虑虚虫虬虮虽虾虿蚀蚁蚂蚕蚝蚬" +
		"蛊蛎蛏蛮蛰蛱蛲蛳蛴蜕蜗蜡蝇蝈蝉蝎蝼蝾螀螨蟏衅衔补衬衮袄袅袆袜袭袯装裆裈裢裣裤裥褛褴襁襕见观觃规觅视觇览觉觊觋觌觍觎觏觐觑觞触觯詟誉誊讠计订讣认讥讦" +
		"讧讨让讪讫训议讯记讱讲讳讴讵讶讷许讹论讻讼讽设访诀证诂诃评诅识诇诈诉诊诋诌词诎诏诐译诒诓诔试诖诗诘诙诚诛诜话诞诟诠诡询诣诤该详诧诨诩诪诫诬语诮误诰" +
		"诱诲诳说诵诶请诸诹诺读诼诽课诿谀谁谂调谄谅谆谇谈谊谋谌谍谎谏谐谑谒谓谔谕谖谗谘谙谚谛谜谝谞谟谠谡谢谣谤谥谦谧谨谩谪谫谬谭谮谯谰谱谲谳谴谵谶谷豮贝贞" +
		"负贠贡财责贤败账货质贩贪贫贬购贮贯贰贱贲贳贴贵贶贷贸费贺贻贼贽贾贿赀赁赂赃资赅赆赇赈赉赊赋赌赍赎赏赐赑赒赓赔赕赖赗赘赙赚赛赜赝赞赟赠赡赢赣赪赵赶趋" +
		"趱趸跃跄跖跞践跶跷跸跹跻踊踌踪踬踯蹑蹒蹰蹿躏躜躯车轧轨轩轪轫转轭轮软轰轱轲轳轴轵轶轷轸轹轺轻轼载轾轿辀辁辂较辄辅辆辇辈辉辊辋辌辍辎辏辐辑辒输辔辕辖" +
		"辗辘辙辚辞辩辫边辽达迁过迈运还这进远违连迟迩迳迹适选逊递逦逻遗遥邓邝邬邮邹邺邻郁郄郏郐郑郓郦郧郸酝酦酱酽酾酿释里鉅鉴銮錾钆钇针钉钊钋钌钍钎钏钐钑钒" +
		"钓钔钕钖钗钘钙钚钛钝钞钟钠钡钢钣钤钥钦钧钨钩钪钫钬钭钮钯钰钱钲钳钴钵钶钷钸钹钺钻钼钽钾钿铀铁铂铃铄铅铆铈铉铊铋铍铎铏铐铑铒铕铗铘铙铚铛铜铝铞铟铠铡" +
		"铢铣铤铥铦铧铨铪铫铬铭铮铯铰铱铲铳铴铵银铷铸铹铺铻铼铽链铿销锁锂锃锄锅锆锇锈锉锊锋锌锍锎锏锐锑锒锓锔锕锖锗错锚锜锞锟锠锡锢锣锤锥锦锨锩锫锬锭键锯锰" +
		"锱锲锳锴锵锶锷锸锹锺锻锼锽锾锿镀镁镂镃镆镇镈镉镊镌镍镎镏镐镑镒镕镖镗镙镚镛镜镝镞镟镠镡镢镣镤镥镦镧镨镩镪镫镬镭镮镯镰镱镲镳镴镶长门闩闪闫闬闭问闯闰" +
		"闱闲闳间闵闶闷闸闹闺闻闼闽闾闿阀阁阂阃阄阅阆阇阈阉阊阋阌阍阎阏阐阑阒阓阔阕阖阗阘阙阚阛队阳阴阵阶际陆陇陈陉陕陧陨险随隐隶隽难雏雠雳雾霁霉霭靓静靥鞑" +
		"鞒鞯鞴韦韧韨韩韪韫韬韵页顶顷顸项顺须顼顽顾顿颀颁颂颃预颅领颇颈颉颊颋颌颍颎颏颐频颒颓颔颕颖颗题颙颚颛颜额颞颟颠颡颢颣颤颥颦颧风飏飐飑飒飓飔飕飖飗飘" +
		"飙飚飞飨餍饤饥饦饧饨饩饪饫饬饭饮饯饰饱饲饳饴饵饶饷饸饹饺饻饼饽饾饿馀馁馂馃馄馅馆馇馈馉馊馋馌馍馎馏馐馑馒馓馔馕马驭驮驯驰驱驲驳驴驵驶驷驸驹驺驻驼驽" +
		"驾驿骀骁骂骃骄骅骆骇骈骉骊骋验骍骎骏骐骑骒骓骔骕骖骗骘骙骚骛骜骝骞骟骠骡骢骣骤骥骦骧髅髋髌鬓魇魉鱼鱽鱾鱿鲀鲁鲂鲄鲅鲆鲇鲈鲉鲊鲋鲌鲍鲎鲏鲐鲑鲒鲓鲔鲕" +
		"鲖鲗鲘鲙鲚鲛鲜鲝鲞鲟鲠鲡鲢鲣鲤鲥鲦鲧鲨鲩鲪鲫鲬鲭鲮鲯鲰鲱鲲鲳鲴鲵鲶鲷鲸鲹鲺鲻鲼鲽鲾鲿鳀鳁鳂鳃鳄鳅鳆鳇鳈鳉鳊鳋鳌鳍鳎鳏鳐鳑鳒鳓鳔鳕鳖鳗鳘鳙鳛鳜鳝鳞鳟" +
		"鳠鳡鳢鳣鸟鸠鸡鸢鸣鸤鸥鸦鸧鸨鸩鸪鸫鸬鸭鸮鸯鸰鸱鸲鸳鸴鸵鸶鸷鸸鸹鸺鸻鸼鸽鸾鸿鹀鹁鹂鹃鹄鹅鹆鹇鹈鹉鹊鹋鹌鹍鹎鹏鹐鹑鹒鹓鹔鹕鹖鹗鹘鹚鹛鹜鹝鹞鹟鹠鹡鹢鹣鹤" +
		"鹥鹦鹧鹨鹩鹪鹫鹬鹭鹯鹰鹱鹲鹳鹴鹾麦麸黄黉黡黩黪黾鼋鼌鼍鼗鼹齄齐齑齿龀龁龂龃龄龅龆龇龈龉龊龋龌龙龚龛龟志制咨只里系范松没尝尝闹面准钟别闲干尽脏拼";

var t = "萬與醜專業叢東絲丟兩嚴喪個爿豐臨為麗舉麼義烏樂喬習鄉書買亂爭於虧雲亙亞產畝親褻嚲億僅從侖倉儀們價眾優夥會傴傘偉傳傷倀倫傖偽佇體餘傭僉俠侶僥偵側僑儈" +
		"儕儂俁儔儼倆儷儉債傾傯僂僨償儻儐儲儺兒兌兗黨蘭關興茲養獸囅內岡冊寫軍農塚馮衝決況凍淨淒涼淩減湊凜幾鳳鳧憑凱擊氹鑿芻劃劉則剛創刪別剗剄劊劌剴劑剮劍剝" +
		"劇勸辦務勱動勵勁勞勢勳猛勩勻匭匱區醫華協單賣盧鹵臥衛卻巹廠廳曆厲壓厭厙廁廂厴廈廚廄廝縣參靉靆雙發變敘疊葉號歎嘰籲後嚇呂嗎唚噸聽啟吳嘸囈嘔嚦唄員咼嗆" +
		"嗚詠哢嚨嚀噝吒噅鹹呱響啞噠嘵嗶噦嘩噲嚌噥喲嘜嗊嘮啢嗩唕喚呼嘖嗇囀齧囉嘽嘯噴嘍嚳囁嗬噯噓嚶囑嚕劈囂謔團園囪圍圇國圖圓聖壙場阪壞塊堅壇壢壩塢墳墜壟壟壚" +
		"壘墾坰堊墊埡墶壋塏堖塒塤堝墊垵塹墮壪牆壯聲殼壺壼處備複夠頭誇夾奪奩奐奮獎奧妝婦媽嫵嫗媯姍薑婁婭嬈嬌孌娛媧嫻嫿嬰嬋嬸媼嬡嬪嬙嬤孫學孿寧寶實寵審憲宮寬" +
		"賓寢對尋導壽將爾塵堯尷屍盡層屭屜屆屬屢屨嶼歲豈嶇崗峴嶴嵐島嶺嶽崠巋嶨嶧峽嶢嶠崢巒嶗崍嶮嶄嶸嶔崳嶁脊巔鞏巰幣帥師幃帳簾幟帶幀幫幬幘幗冪襆幹並廣莊慶廬" +
		"廡庫應廟龐廢廎廩開異棄張彌弳彎彈強歸當錄彠彥徹徑徠禦憶懺憂愾懷態慫憮慪悵愴憐總懟懌戀懇惡慟懨愷惻惱惲悅愨懸慳憫驚懼慘懲憊愜慚憚慣湣慍憤憒願懾憖怵懣" +
		"懶懍戇戔戲戧戰戩戶紮撲扡執擴捫掃揚擾撫拋摶摳掄搶護報擔擬攏揀擁攔擰撥擇掛摯攣掗撾撻挾撓擋撟掙擠揮撏撈損撿換搗據撚擄摑擲撣摻摜摣攬撳攙擱摟攪攜攝攄擺" +
		"搖擯攤攖撐攆擷擼攛擻攢敵斂數齋斕鬥斬斷無舊時曠暘曇晝曨顯晉曬曉曄暈暉暫曖劄術樸機殺雜權條來楊榪傑極構樅樞棗櫪梘棖槍楓梟櫃檸檉梔柵標棧櫛櫳棟櫨櫟欄樹" +
		"棲樣欒棬椏橈楨檔榿橋樺檜槳樁夢檮棶檢欞槨櫝槧欏橢樓欖櫬櫚櫸檟檻檳櫧橫檣櫻櫫櫥櫓櫞簷檁歡歟歐殲歿殤殘殞殮殫殯毆毀轂畢斃氈毿氌氣氫氬氳彙漢汙湯洶遝溝沒" +
		"灃漚瀝淪滄渢溈滬濔濘淚澩瀧瀘濼瀉潑澤涇潔灑窪浹淺漿澆湞溮濁測澮濟瀏滻渾滸濃潯濜塗湧濤澇淶漣潿渦溳渙滌潤澗漲澀澱淵淥漬瀆漸澠漁瀋滲溫遊灣濕潰濺漵漊潷" +
		"滾滯灩灄滿瀅濾濫灤濱灘澦濫瀠瀟瀲濰潛瀦瀾瀨瀕灝滅燈靈災燦煬爐燉煒熗點煉熾爍爛烴燭煙煩燒燁燴燙燼熱煥燜燾煆糊溜愛爺牘犛牽犧犢強狀獷獁猶狽麅獮獰獨狹獅" +
		"獪猙獄猻獫獵獼玀豬貓蝟獻獺璣璵瑒瑪瑋環現瑲璽瑉玨琺瓏璫琿璡璉瑣瓊瑤璦璿瓔瓚甕甌電畫暢佘疇癤療瘧癘瘍鬁瘡瘋皰屙癰痙癢瘂癆瘓癇癡癉瘮瘞瘺癟癱癮癭癩癬癲" +
		"臒皚皺皸盞鹽監蓋盜盤瞘眥矓著睜睞瞼瞞矚矯磯礬礦碭碼磚硨硯碸礪礱礫礎硜矽碩硤磽磑礄確鹼礙磧磣堿镟滾禮禕禰禎禱禍稟祿禪離禿稈種積稱穢穠穭稅穌穩穡窮竊竅" +
		"窯竄窩窺竇窶豎競篤筍筆筧箋籠籩築篳篩簹箏籌簽簡籙簀篋籜籮簞簫簣簍籃籬籪籟糴類秈糶糲粵糞糧糝餱緊縶糸糾紆紅紂纖紇約級紈纊紀紉緯紜紘純紕紗綱納紝縱綸紛" +
		"紙紋紡紵紖紐紓線紺絏紱練組紳細織終縐絆紼絀紹繹經紿綁絨結絝繞絰絎繪給絢絳絡絕絞統綆綃絹繡綌綏絛繼綈績緒綾緓續綺緋綽緔緄繩維綿綬繃綢綯綹綣綜綻綰綠綴" +
		"緇緙緗緘緬纜緹緲緝縕繢緦綞緞緶線緱縋緩締縷編緡緣縉縛縟縝縫縗縞纏縭縊縑繽縹縵縲纓縮繆繅纈繚繕繒韁繾繰繯繳纘罌網羅罰罷羆羈羥羨翹翽翬耮耬聳恥聶聾職聹" +
		"聯聵聰肅腸膚膁腎腫脹脅膽勝朧腖臚脛膠脈膾髒臍腦膿臠腳脫腡臉臘醃膕齶膩靦膃騰臏臢輿艤艦艙艫艱豔艸藝節羋薌蕪蘆蓯葦藶莧萇蒼苧蘇檾蘋莖蘢蔦塋煢繭荊薦薘莢" +
		"蕘蓽蕎薈薺蕩榮葷滎犖熒蕁藎蓀蔭蕒葒葤藥蒞蓧萊蓮蒔萵薟獲蕕瑩鶯蓴蘀蘿螢營縈蕭薩蔥蕆蕢蔣蔞藍薊蘺蕷鎣驀薔蘞藺藹蘄蘊藪槁蘚虜慮虛蟲虯蟣雖蝦蠆蝕蟻螞蠶蠔蜆" +
		"蠱蠣蟶蠻蟄蛺蟯螄蠐蛻蝸蠟蠅蟈蟬蠍螻蠑螿蟎蠨釁銜補襯袞襖嫋褘襪襲襏裝襠褌褳襝褲襇褸襤繈襴見觀覎規覓視覘覽覺覬覡覿覥覦覯覲覷觴觸觶讋譽謄訁計訂訃認譏訐" +
		"訌討讓訕訖訓議訊記訒講諱謳詎訝訥許訛論訩訟諷設訪訣證詁訶評詛識詗詐訴診詆謅詞詘詔詖譯詒誆誄試詿詩詰詼誠誅詵話誕詬詮詭詢詣諍該詳詫諢詡譸誡誣語誚誤誥" +
		"誘誨誑說誦誒請諸諏諾讀諑誹課諉諛誰諗調諂諒諄誶談誼謀諶諜謊諫諧謔謁謂諤諭諼讒諮諳諺諦謎諞諝謨讜謖謝謠謗諡謙謐謹謾謫譾謬譚譖譙讕譜譎讞譴譫讖穀豶貝貞" +
		"負貟貢財責賢敗賬貨質販貪貧貶購貯貫貳賤賁貰貼貴貺貸貿費賀貽賊贄賈賄貲賃賂贓資賅贐賕賑賚賒賦賭齎贖賞賜贔賙賡賠賧賴賵贅賻賺賽賾贗讚贇贈贍贏贛赬趙趕趨" +
		"趲躉躍蹌蹠躒踐躂蹺蹕躚躋踴躊蹤躓躑躡蹣躕躥躪躦軀車軋軌軒軑軔轉軛輪軟轟軲軻轤軸軹軼軤軫轢軺輕軾載輊轎輈輇輅較輒輔輛輦輩輝輥輞輬輟輜輳輻輯轀輸轡轅轄" +
		"輾轆轍轔辭辯辮邊遼達遷過邁運還這進遠違連遲邇逕跡適選遜遞邐邏遺遙鄧鄺鄔郵鄒鄴鄰鬱郤郟鄶鄭鄆酈鄖鄲醞醱醬釅釃釀釋裏钜鑒鑾鏨釓釔針釘釗釙釕釷釺釧釤鈒釩" +
		"釣鍆釹鍚釵鈃鈣鈈鈦鈍鈔鍾鈉鋇鋼鈑鈐鑰欽鈞鎢鉤鈧鈁鈥鈄鈕鈀鈺錢鉦鉗鈷缽鈳鉕鈽鈸鉞鑽鉬鉭鉀鈿鈾鐵鉑鈴鑠鉛鉚鈰鉉鉈鉍鈹鐸鉶銬銠鉺銪鋏鋣鐃銍鐺銅鋁銱銦鎧鍘" +
		"銖銑鋌銩銛鏵銓鉿銚鉻銘錚銫鉸銥鏟銃鐋銨銀銣鑄鐒鋪鋙錸鋱鏈鏗銷鎖鋰鋥鋤鍋鋯鋨鏽銼鋝鋒鋅鋶鐦鐧銳銻鋃鋟鋦錒錆鍺錯錨錡錁錕錩錫錮鑼錘錐錦鍁錈錇錟錠鍵鋸錳" +
		"錙鍥鍈鍇鏘鍶鍔鍤鍬鍾鍛鎪鍠鍰鎄鍍鎂鏤鎡鏌鎮鎛鎘鑷鐫鎳鎿鎦鎬鎊鎰鎔鏢鏜鏍鏰鏞鏡鏑鏃鏇鏐鐔钁鐐鏷鑥鐓鑭鐠鑹鏹鐙鑊鐳鐶鐲鐮鐿鑔鑣鑞鑲長門閂閃閆閈閉問闖閏" +
		"闈閑閎間閔閌悶閘鬧閨聞闥閩閭闓閥閣閡閫鬮閱閬闍閾閹閶鬩閿閽閻閼闡闌闃闠闊闋闔闐闒闕闞闤隊陽陰陣階際陸隴陳陘陝隉隕險隨隱隸雋難雛讎靂霧霽黴靄靚靜靨韃" +
		"鞽韉韝韋韌韍韓韙韞韜韻頁頂頃頇項順須頊頑顧頓頎頒頌頏預顱領頗頸頡頰頲頜潁熲頦頤頻頮頹頷頴穎顆題顒顎顓顏額顳顢顛顙顥纇顫顬顰顴風颺颭颮颯颶颸颼颻飀飄" +
		"飆飆飛饗饜飣饑飥餳飩餼飪飫飭飯飲餞飾飽飼飿飴餌饒餉餄餎餃餏餅餑餖餓餘餒餕餜餛餡館餷饋餶餿饞饁饃餺餾饈饉饅饊饌饢馬馭馱馴馳驅馹駁驢駔駛駟駙駒騶駐駝駑" +
		"駕驛駘驍罵駰驕驊駱駭駢驫驪騁驗騂駸駿騏騎騍騅騌驌驂騙騭騤騷騖驁騮騫騸驃騾驄驏驟驥驦驤髏髖髕鬢魘魎魚魛魢魷魨魯魴魺鮁鮃鯰鱸鮋鮓鮒鮊鮑鱟鮍鮐鮭鮚鮳鮪鮞" +
		"鮦鰂鮜鱠鱭鮫鮮鮺鯗鱘鯁鱺鰱鰹鯉鰣鰷鯀鯊鯇鮶鯽鯒鯖鯪鯕鯫鯡鯤鯧鯝鯢鯰鯛鯨鯵鯴鯔鱝鰈鰏鱨鯷鰮鰃鰓鱷鰍鰒鰉鰁鱂鯿鰠鼇鰭鰨鰥鰩鰟鰜鰳鰾鱈鱉鰻鰵鱅鰼鱖鱔鱗鱒" +
		"鱯鱤鱧鱣鳥鳩雞鳶鳴鳲鷗鴉鶬鴇鴆鴣鶇鸕鴨鴞鴦鴒鴟鴝鴛鴬鴕鷥鷙鴯鴰鵂鴴鵃鴿鸞鴻鵐鵓鸝鵑鵠鵝鵒鷳鵜鵡鵲鶓鵪鶤鵯鵬鵮鶉鶊鵷鷫鶘鶡鶚鶻鶿鶥鶩鷊鷂鶲鶹鶺鷁鶼鶴" +
		"鷖鸚鷓鷚鷯鷦鷲鷸鷺鸇鷹鸌鸏鸛鸘鹺麥麩黃黌黶黷黲黽黿鼂鼉鞀鼴齇齊齏齒齔齕齗齟齡齙齠齜齦齬齪齲齷龍龔龕龜誌製谘隻裡係範鬆冇嚐嘗鬨麵準鐘彆閒乾儘臟拚";

function string_prototype_s2t(str) {
	var k = '';
	for (var i = 0; i < str.length; i++)
		k += (s.indexOf(str.charAt(i)) == -1) ? str.charAt(i) : t.charAt(s.indexOf(str.charAt(i)));
	return k;
}

function string_prototype_t2s(str) {
	var k = '';
	for (var i = 0; i < str.length; i++)
		k += (t.indexOf(str.charAt(i)) == -1) ? str.charAt(i) : s.charAt(t.indexOf(str.charAt(i)));
	return k;
}

window.onscroll = function() {
	var t = document.documentElement.scrollTop || document.body.scrollTop;
	var nav = document.getElementById("nav");
	
	if (t >= 600) {
		nav.style.position = "fixed";
		nav.style.top = "0px";
		$("#totop").show();
		
		if (t <= 1100) {
			for (var i = 1; i <= 11; i++) {
				if (i != 1) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu1").attr("class", "kuai");
			}
		} else if (t > 1100 && t <= 1600) {
			for (var i = 1; i <= 11; i++) {
				if (i != 2) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu2").attr("class", "kuai");
			}
		} else if (t > 1600 && t <= 2050) {
			for (var i = 1; i <= 11; i++) {
				if (i != 3) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu3").attr("class", "kuai");
			}
		} else if (t > 2050 && t <= 2500) {
			for (var i = 1; i <= 11; i++) {
				if (i != 4) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu4").attr("class", "kuai");
			}
		} else if (t > 2500 && t <= 2930) {
			for (var i = 1; i <= 11; i++) {
				if (i != 5) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu5").attr("class", "kuai");
			}
		} else if (t > 2930 && t <= 3370) {
			for (var i = 1; i <= 11; i++) {
				if (i != 6) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu6").attr("class", "kuai");
			}
		} else if (t > 3370 && t <= 3900) {
			for (var i = 1; i <= 11; i++) {
				if (i != 7) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu7").attr("class", "kuai");
			}
		} else if (t > 3900 && t <= 4330) {
			for (var i = 1; i <= 11; i++) {
				if (i != 8) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu8").attr("class", "kuai");
			}
		} else if (t > 4330 && t <= 4750) {
			for (var i = 1; i <= 11; i++) {
				if (i != 9) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu9").attr("class", "kuai");
			}
		} else if (t > 4750 && t <= 5150) {
			for (var i = 1; i <= 11; i++) {
				if (i != 10) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu10").attr("class", "kuai");
			}
		} else if (t > 5150) {
			for (var i = 1; i <= 11; i++) {
				if (i != 11) {
					$("#menu" + i).attr("class", "anniu02");
				}
				$("#menu11").attr("class", "kuai");
			}
		}
	} else {
		nav.style.position = "absolute";
		nav.style.top = "623px";
		$("#totop").hide();
	}
};