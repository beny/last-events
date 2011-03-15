function initialize() {

  // inicializace mapy
  var myLatlng = new google.maps.LatLng(43.6, 0.7);
  var myOptions = {
    zoom: 2,
    center: myLatlng,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  }
  var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);


  // testovaci body
  var point1 = new google.maps.LatLng(49.1943, 16.6100);
  var point2 = new google.maps.LatLng(50.082, 14.431);
  var point3 = new google.maps.LatLng(51.45, -0.18);
  var point4 = new google.maps.LatLng(38.87, -77.06);

  // testovaci path
  var pathCoords = [point1, point2, point3, point4,];
  var path = new google.maps.Polyline({
    path: pathCoords,
    strokeColor: "#db1302",
    strokeOpacity: 0.5,
    strokeWeight: 5
  });
  path.setMap(map);


  // inicializace bodu
  var image = new google.maps.MarkerImage('images/pin.png', null, null, new google.maps.Point(8, 35));
  
  var contentString = '<div id="content">'+
      '<div id="siteNotice">'+
      '</div>'+
      '<h1 id="firstHeading" class="firstHeading">Uluru</h1>'+
      '<div id="bodyContent">'+
      '<p><b>Uluru</b>, also referred to as <b>Ayers Rock</b>, is a large ' +
      'sandstone rock formation in the southern part of the '+
      'Northern Territory, central Australia. It lies 335 km (208 mi) '+
      'south west of the nearest large town, Alice Springs; 450 km '+
      '(280 mi) by road. Kata Tjuta and Uluru are the two major '+
      'features of the Uluru - Kata Tjuta National Park. Uluru is '+
      'sacred to the Pitjantjatjara and Yankunytjatjara, the '+
      'Aboriginal people of the area. It has many springs, waterholes, '+
      'rock caves and ancient paintings. Uluru is listed as a World '+
      'Heritage Site.</p>'+
      '<p>Attribution: Uluru, <a href="http://en.wikipedia.org/w/index.php?title=Uluru&oldid=297882194">'+
      'http://en.wikipedia.org/w/index.php?title=Uluru</a> (last visited June 22, 2009).</p>'+
      '</div>'+
      '</div>';

  var infowindow = new google.maps.InfoWindow({
      content: contentString
  });
  
  var marker1 = new google.maps.Marker({
    position: point1,
    map: map,
    icon: image
  });
  google.maps.event.addListener(marker1, 'click', function() {
    infowindow.open(map,marker1);
  });
  
  
  var marker2 = new google.maps.Marker({
    position: point2,
    map: map,
    icon: image
  });
  google.maps.event.addListener(marker2, 'click', function() {
    infowindow.open(map,marker2);
  });
  
  var marker3 = new google.maps.Marker({
    position: point3,
    map: map,
    icon: image
  });
  google.maps.event.addListener(marker3, 'click', function() {
    infowindow.open(map,marker3);
  });
  
  var marker4 = new google.maps.Marker({
    position: point4,
    map: map,
    icon: image
  });
  google.maps.event.addListener(marker4, 'click', function() {
    infowindow.open(map,marker4);
  });
  
}