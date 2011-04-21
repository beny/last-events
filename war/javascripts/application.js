$(document).ready(function(){ 

  // inicializace pomocnych promennych
  var d=new Date();
  var month=new Array(12);
  month[0]="Jan";
  month[1]="Feb";
  month[2]="Mar";
  month[3]="Apr";
  month[4]="May";
  month[5]="Jun";
  month[6]="Jul";
  month[7]="Aug";
  month[8]="Sep";
  month[9]="Oct";
  month[10]="Nov";
  month[11]="Dec";

  // inicializace mapy
	var myLatlng = new google.maps.LatLng(43.6, 0.7);
	var myOptions = {
			zoom: 2,
			center: myLatlng,
			mapTypeId: google.maps.MapTypeId.ROADMAP
	}
	var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);

	// obrazek pointu
	var image = new google.maps.MarkerImage('images/pin.png', null, null, new google.maps.Point(8, 35));

	// inicializace poli pro zobrazeni bodu
	var points = new Array();
	var pathPoints = new Array();
	var markers = new Array();
	var bounds = new google.maps.LatLngBounds();
	var path = null;

	// fce vymazavajici vse z mapy
	function clearOverlays() {
		points = [];
		pathPoints = [];
		bounds = new google.maps.LatLngBounds();
		if(path != null) path.setMap(null);
		if (markers) {
			for (i in markers) {
				markers[i].setMap(null);
			}
		}
		markers = [];
		
		// vycisteni seznamu s vysledky
		$("#events").empty();
	}

	// vyhledavani bodu podle id eventu
	function convertIdToCoordinates(id){
		for(i in markers){
			if(markers[i].id == id.substr(5))
				return markers[i].getPosition();
		}
	}

	// autocomplete
	$("#q").autocomplete({
	  open: function(event, ui) { 
	    $(".ui-slider-handle").css("z-index", -1); 
	  },
    close: function(event, ui) { 
      $(".ui-slider-handle").css("z-index", 2); 
    },
		source: function(request, response) {
			$.ajax({
				url: "api/search/artist/"+request.term,//request url
				dataType: "json",
				success: function(data) {
					response($.map(data, function(item) {
						return {
							label: item,
							value: item
						}
					}))
				}
			})
		},
		select: function( event, ui ) {

			// vycisteni mapy
			clearOverlays();

			$.getJSON("/api/artist/"+ui.item.value+"/"+parseInt($("#artist-day").html()), function (data){
				
				// TODO pokud jsou data prazdna nehybej s mapou a nedelej jine veci
				// pokud existuji nejaka data	
				
				for(i in data){
					var title = data[i].title;

					var lat = data[i].venue.lat;
					var lon = data[i].venue.lon;
					var id = data[i].id;
					var date = new Date(data[i].date);
					var city = data[i].venue.city;
					var venue = data[i].venue.name;
					var country = data[i].venue.country;

					// preskoc pokud nejsou zadany souradnice
					if(lat == 0 && lon == 0) continue;

					// pridani bodu do mapy a vsech pomocnych poli
					var p = new google.maps.LatLng(lat, lon);
					points.push(p);
					pathPoints.push(p);
					bounds.extend(p);
					markers.push(new google.maps.Marker({
						position: p,
						map: map,
						icon: image,
						id: id
					}));

					// seznam eventu v liste
					$("#events").append(
					  '<li><a id="event'+id+'" href="#" title="'+title+'"><span class="calSheet calSheetSmall"><span class="month">'+month[date.getMonth()]+'</span><span class="day">'+date.getDate()+'</span></span><strong class="summary">'+title+'</strong></a><small class="location adr">'+venue+', '+city+', '+country+'</small></li>'
					  )
          $("event"+id).click(function(){
            map.panTo(convertIdToCoordinates($(this).attr("id")));
          });
				}

				// vytvoreni a vykresleni na mape
				path = new google.maps.Polyline({
					path: pathPoints,
					strokeColor: "#db1302",
					strokeOpacity: 0.5,
					strokeWeight: 5
				});
				path.setMap(map);

				// zacentrovani na body
				map.fitBounds(bounds);

			});
			return false;
		}
	});
	
	// inicializace akordeonu
  $("#accordion").accordion({
    icons: false,
    autoHeight: false
  });
  
  // vymazani vseho pri zmene hledani
  $("#accordion.h3").click(function(){
    clearOverlays();
  });
  
  // inicializace slideru
  $("#slider-artist-day").slider({
    range: "min",
    value: 5,
    min: 1,
    max: 50,
    slide: function( event, ui ) {
      $("#artist-day").html(ui.value);
    }
  });
  $("#artist-day").html($("#slider-artist-day").slider("value"));

  $("#slider-place-distance").slider({
    range: "min",
    value: 10,
    min: 1,
    max: 1000,
    slide: function( event, ui ) {
      $("#place-distance").html(ui.value);
    }
  });
  $("#place-distance").html($("#slider-place-distance").slider("value"));
  
});