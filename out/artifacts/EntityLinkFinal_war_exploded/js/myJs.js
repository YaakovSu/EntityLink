function changeHome(){
  
  $("#tt1").attr("class","main");//改变class
  $("#tt2").attr("class","");//改变id
  $("#tt3").attr("class","");//改变id
  $("#tt4").attr("class","");//改变id
  // $("#tt2").css('display','non');
    document.getElementById("tt1").style.display="block";
  document.getElementById("tt2").style.display="none";
  document.getElementById("tt3").style.display="none";
  document.getElementById("tt4").style.display="none";
   // $("#tt2").css('display','block');
}
 function changeAbout(){
  $("#tt1").attr("class","");//改变id
  $("#tt2").attr("class","main");//改变class
    $("#tt3").attr("class","");//改变id
  $("#tt4").attr("class","");//改变id
  // $("#tt2").css('display','non');
  document.getElementById("tt1").style.display="none";
     document.getElementById("tt2").style.display="block";
  document.getElementById("tt3").style.display="none";
  document.getElementById("tt4").style.display="none";
   // $("#tt2").css('display','block');
}

 function changeProjects(){
  $("#tt1").attr("class","");//改变id
  $("#tt2").attr("class","");//改变class
    $("#tt3").attr("class","main");//改变id
  $("#tt4").attr("class","");//改变id
  // $("#tt2").css('display','non');
  document.getElementById("tt1").style.display="none";
  document.getElementById("tt2").style.display="none";
     document.getElementById("tt3").style.display="block";
  document.getElementById("tt4").style.display="none";
   // $("#tt2").css('display','block');
}

 function changeBlog(){
  $("#tt1").attr("class","");//改变id
  $("#tt2").attr("class","");//改变class
    $("#tt3").attr("class","");//改变id
  $("#tt4").attr("class","main");//改变id
  // $("#tt2").css('display','non');
  document.getElementById("tt1").style.display="none";
  document.getElementById("tt2").style.display="none";
  document.getElementById("tt3").style.display="none";
     document.getElementById("tt4").style.display="block";
   // $("#tt2").css('display','block');
}

