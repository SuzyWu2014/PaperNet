$(document).ready(function(){
	 $.ajax({
		 url:"https://api.datamarket.azure.com/MRC/MicrosoftAcademic/v2/Paper?$filter=ID%20eq%2015",
		 type:"get",
		 success: function(result){
			 
		 }
	
	 });
});