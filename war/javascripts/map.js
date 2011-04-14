//inicializace mapy
$(document).ready(function(){ 
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
		source: function(request, response) {
			$.ajax({
				url: "api/names/"+request.term,//request url
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

			// vycisteni seznamu s vysledky
			$("#events").empty();

			// vycisteni mapy
			clearOverlays();

			$.getJSON("/api/artist/"+ui.item.value, function (data){
				
				// TODO pokud jsou data prazdna nehybej s mapou a nedelej jine veci
				// pokud existuji nejaka data	
				for(i in data){

					var title = data[i].title;

					var lat = data[i].venue.lat;
					var lon = data[i].venue.lon;
					var id = data[i].id;
					var date = new Date(data[i].date);
					var city = data[i].venue.city;

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
					$("#events").append("<p id=\"event"+ id +"\">" + date.toDateString() + " - " +
							title + " @ " + city + "</p>")
					$("#event"+id).click(function (){
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
});