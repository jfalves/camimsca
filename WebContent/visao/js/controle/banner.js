$(document).ready(
	function() {
		function imageresize() {
			var contentwidth = $('#header').width();
			if ((contentwidth) > '780'){
				$('.image').attr('src','img/banner_1024.png');
			} else if ((contentwidth) > '960'){
				$('.image').attr('src','img/banner_1280.png');
			} else {
				$('.image').attr('src','img/banner.png');
			}
		}
	
		function gridresize() {
			$('#list').fluidGrid({base:'#resultado', offset: -1});
			$('#search-list').fluidGrid({base:'#resultado', offset: -1});
		}
	
		imageresize();	
		gridresize();

		$(window).bind("resize",
			function(){
				imageresize();
				gridresize();
			}
		);
	}
);