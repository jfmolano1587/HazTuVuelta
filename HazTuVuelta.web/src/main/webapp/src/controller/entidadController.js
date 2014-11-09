/* ========================================================================
 * Copyright 2014 Arquidalgos
 *
 * Licensed under the MIT, The MIT License (MIT)
 * Copyright (c) 2014 Arquidalgos
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 * ========================================================================
 
 
 Source generated by CrudMaker version 1.0.0.201408112050
 
 */
define(['controller/_entidadController', 'delegate/entidadDelegate', 'lib/gmaps'], function () {
    App.Controller.EntidadController = App.Controller._EntidadController.extend({
        postInit: function (options) {
            var self = this;
            Backbone.on(this.componentId + '-entidad-verSedes', function (params) {
                self.verSedes();
            });
            Backbone.on(this.componentId + '-reservar', function (params) {
                self.redirectFrame('usuario.html');
            });
            Backbone.on(this.componentId + '-hideMap', function (params) {
                self.hideMap();
            });
            Backbone.on(this.componentId + 'select', function (params) {
                console.log('Seleción de una hora');
                self.horaSelecionada = params.id;
                self.resizeMap();
            });
            Backbone.on('reservar', function (params) {
                console.log('Reservar Turno');
                self.reservar();
            });

            document.getElementById("pnlReservarTurno").style.display = 'none';
            document.getElementById("main").style.display = 'none';

            this.initMap();
            this.getLocation();
            this.bool = false;
            if (this.isMobile())
            {
                document.getElementById("map").style.width = '100%';
            }

            this.listTemplate = _.template($('#entidadList').html());
        },
        reservar: function () {
            var self = this;
            var model = $('#infoUsuario').serializeObject();

            self.cita = new App.Model.CitaModel();
            self.cita.set("horaInicInt", this.horaSelecionada);
            self.cita.set("name", model.cedula);
            self.cita.set("sedecitaId", this.idSedeSelecionada);

            console.log('info nueva cita: ' + JSON.stringify(self.cita));

            this.entidadDelegate = new App.Delegate.EntidadDelegate();
            this.entidadDelegate.reservarTurnoDelagate(
                    self.cita,
                    function (data) {
                        self.actualizarDatosReserva();
                        console.log(JSON.stringify(data));
                    },
                    function (data) {
                        self.actualizarDatosReserva();
                        console.log('Error al reservar turno: ' + JSON.stringify(data));
                    }
            );

        },
        actualizarDatosReserva: function () {
            var self = this;
            document.getElementById("panelReservar").style.display = 'none';
            this.entidadDelegate = new App.Delegate.EntidadDelegate();
            this.entidadDelegate.darHoraAtencionDelegate(
                    self.cita.get("name"),
                    function (data) {
                        console.log('turno sucursal: ' + JSON.stringify(data));
                        $('#horaAtencion').html("<p>" + data + "</p>");
                    },
                    function (data) {

                        console.log('Error en el refrescar info sucursal: ' + JSON.stringify(data));
                    }
            );

        },
        hideMap: function () {
            console.log('Hide Map');
            var elem = document.getElementById("contenedorMap");
            if (this.bool)
            {
                elem.style.display = 'block';

            }
            else
            {
                elem.style.display = 'none';
            }
            this.bool = !this.bool;


        },
        resizeMap: function () {
            console.log('Hide Map');
            document.getElementById("map").style.height = "227px";
            document.getElementById("infoColores").style.display = 'none';
            document.getElementById("contenedor2Map").style.height = "327px";
            $("#pnlReservarTurno").show();
            //document.getElementById("pnlReservarTurno").show();
            //.style.display = 'flesh';

        },
        isMobile: function () {

            return (
                    (navigator.userAgent.match(/Android/i)) ||
                    (navigator.userAgent.match(/webOS/i)) ||
                    (navigator.userAgent.match(/iPhone/i)) ||
                    (navigator.userAgent.match(/iPod/i)) ||
                    (navigator.userAgent.match(/iPad/i)) ||
                    (navigator.userAgent.match(/BlackBerry/))
                    );
        },
        getLocation: function () {
            var self = this;
            GMaps.geolocate({
                success: function (position) {
                    self.map.setCenter(position.coords.latitude, position.coords.longitude);


                    // Creating marker of user location
                    self.map.addMarker({
                        lat: position.coords.latitude,
                        lng: position.coords.longitude,
                        title: 'Tu',
                        icon: 'img/punteroUsuario.png',
                        click: function (e) {
                        },
                        infoWindow: {
                            content: '<p>You are here!</p>'
                        }
                    });
                },
                error: function (error) {
                    alert('Geolocation failed: ' + error.message);
                },
                not_supported: function () {
                    alert("Your browser does not support geolocation");
                }
            });
        },
        initMap: function () {

            var self = this;
            //Variables-----------------------------------
            //Colores Marcadores
            var marcadorVerde = 'http://library.csun.edu/images/google_maps/marker-green.png';
            var marcadorRojo = 'http://inthespace.net/graphics/icon_google_maps2.png';
            var marcadorAmarillo = 'http://static.batchgeo.com/images/icons/yellow_shadow_Marker.png';
            var coloresMarcadores = [marcadorVerde, marcadorAmarillo, marcadorRojo];
            var coloresHorario = ["gray", "#2dcc70", "#e67d22", "#e74b3c", "#27ae61", "#d25400", "#c1392b"];
            var clasesHorario = ["horarioActivado", "horarioDesactivado"];
            var anteriorHorarioSel = -1;

            //Lista tramites
            var lisaTramites;

            //Lista sucursares
            var sucursales = [
                {"id": 1, "nombre": "SIM Cedritos", "lat": 4.7299483948472725, "lng": -74.04592265136054, "estado": 0, "entidad": 0, "dire": "Calle 147 #19-66 L31", "turnoAtencion": 20, "turnoPedido": 22, "horas": [0, 0, 0, 1, 2, 3, 2, 1, 1, 1, 1]},
                {"id": 2, "nombre": "SIM Autopista 106", "lat": 4.694117579050114, "lng": -74.05666892177871, "estado": 1, "entidad": 0, "dire": "Autopista Norte #106-25/ Piso 2", "turnoAtencion": 10, "turnoPedido": 30, "horas": [0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1]},
                {"id": 3, "nombre": "SIM Nizza", "lat": 4.708157148841132, "lng": -74.07222036964706, "estado": 2, "entidad": 0, "dire": "Transversal 60 #124-20 Int. 5", "turnoAtencion": 1, "turnoPedido": 50, "horas": [0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1]},
                {"id": 4, "nombre": "SIM Siete de Agosto", "lat": 4.658609066756216, "lng": -74.0692739630442, "estado": 2, "entidad": 0, "dire": "Calle 68 #23-27", "turnoAtencion": 33, "turnoPedido": 34, "horas": [0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1]},
                {"id": 5, "nombre": "SIM Chapinero", "lat": 4.645954660919973, "lng": -74.06414423830321, "estado": 0, "entidad": 0, "dire": "Calle 59 #13-97", "turnoAtencion": 15, "turnoPedido": 18, "horas": [0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1]},
                {"id": 6, "nombre": "SIM Galer&#237as", "lat": 4.640811028448222, "lng": -74.07519292778781, "estado": 1, "entidad": 0, "dire": "Calle 52 #25-35", "turnoAtencion": 80, "turnoPedido": 100, "horas": [0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1]},
                {"id": 7, "nombre": "SIM San Diego", "lat": 4.612054519536214, "lng": -74.0695636416178, "estado": 2, "entidad": 0, "dire": "Carrera 7 #26-16 L5", "turnoAtencion": 2, "turnoPedido": 5, "horas": [0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1]},
                {"id": 8, "nombre": "SIM Terminal", "lat": 4.65322291854579, "lng": -74.11504854209235, "estado": 1, "entidad": 0, "dire": "Diagonal 23 #69-60 M&#243dulo L122", "turnoAtencion": 2, "turnoPedido": 20, "horas": [0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1]},
                {"id": 9, "nombre": "SIM Ricaurte", "lat": 4.611026548823848, "lng": -74.0885369176369, "estado": 0, "entidad": 0, "dire": "Calle 13 #26-66 y/o 25-88", "turnoAtencion": 43, "turnoPedido": 45, "horas": [0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1]},
                {"id": 10, "nombre": "SIM Restrepo", "lat": 4.583770747387098, "lng": -74.10166230745128, "estado": 2, "entidad": 0, "dire": "Carrera 17 #19A-32 Sur", "turnoAtencion": 65, "turnoPedido": 110, "horas": [0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1]},
                {"id": 11, "nombre": "SIM Sur", "lat": 4.6144392989245215, "lng": -74.13515169865897, "estado": 0, "entidad": 0, "dire": "Carrera 69 Bis #28-21 Sur", "turnoAtencion": 71, "turnoPedido": 71, "horas": [0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1]},
                {"id": 12, "nombre": "SIM Sevillana", "lat": 4.591812981502772, "lng": -74.14576318807414, "estado": 1, "entidad": 0, "dire": "Carrera 57 #45A-08 Sur Int. 1", "turnoAtencion": 3, "turnoPedido": 22, "horas": [0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1]}
            ];

            var celdasHorarios = [
                {"id": 0, "nombre": "7:00 AM", "estado": 0},
                {"id": 1, "nombre": "8:00 AM", "estado": 0},
                {"id": 2, "nombre": "9:00 AM", "estado": 0},
                {"id": 3, "nombre": "10:00 AM", "estado": 0},
                {"id": 4, "nombre": "11:00 AM", "estado": 0},
                {"id": 5, "nombre": "12:00 AM", "estado": 0},
                {"id": 6, "nombre": "1:00 PM", "estado": 0},
                {"id": 7, "nombre": "2:00 PM", "estado": 0},
                {"id": 8, "nombre": "3:00 PM", "estado": 0},
                {"id": 9, "nombre": "4:00 PM", "estado": 0},
                {"id": 100, "nombre": "5:00 PM", "estado": 0}
            ];

            //celdasHorario[i].estado=1;


            //Mapa
            self.map = new GMaps({
                div: '#map',
                lat: 4.657556435256028,
                lng: -74.06511385686214,
                zoom: 11,
                click: function (e) {

                    //alert(map.getCenter())
                },
                dragend: function (e) {
                    //alert('drag')
                }
            });


            //Marcadores
            //Inicializar marcadores
            var marcadores = [];
            for (var i = 0; i < sucursales.length; i++)
            {
                var marker1 = self.map.createMarker({
                    lat: sucursales[i].lat,
                    lng: sucursales[i].lng,
                    title: "" + i,
                    icon: "",
                    infoWindow: {
                        content: '<p>' + sucursales[i].nombre + '</p>'
                    },
                    click: function (e) {
                        var marcadorActual = parseInt(this.getTitle());
                        refrescarInfoSucursal(marcadorActual);
                        self.idSedeSelecionada = sucursales[marcadorActual].id;
                    }
                });
                marcadores[marcadores.length] = marker1;
                //alert(sucursales[0].entidad);
            }

            var entidadActual = parseInt($('.selectTramites').val());
            mostrarMarcadoresEntidad(entidadActual);

            //Mostrar marcadores
            $('.selectTramites').change(function () {
                //var entidadActual = parseInt($('.selectTramites').val());
                mostrarMarcadoresEntidad();
            });

            function calcularTiempoEsperaSucursal(iMarcador) {
                return tiempoEspera = parseInt((sucursales[iMarcador].turnoPedido - sucursales[iMarcador].turnoAtencion) * 5);//5 minutos por turno
            }
            ;

            function calcularEstadoSucursal(iMarcador) {
                var tiempo = calcularTiempoEsperaSucursal(iMarcador);
                if (tiempo < 10)
                {
                    return 0;
                }
                else if (tiempo < 20)
                {
                    return 1;
                }
                else
                {
                    return 2;
                }
            }
            ;

            function mostrarMarcadoresEntidad()
            {
                self.map.removeMarkers();
                for (var i = 0; i < marcadores.length; i++)
                {
                    //if (sucursales[i].entidad==iEntidad)
                    //{
                    var estado = calcularEstadoSucursal(i);
                    marcadores[i].icon = coloresMarcadores[estado];
                    self.map.addMarker(marcadores[i]);
                    //}
                }
            }
            ;

            function refrescarInfoSucursal(iMarcadorActual)
            {
                this.entidadDelegate = new App.Delegate.EntidadDelegate();
                this.entidadDelegate.darTurnoAtendidoDelegate(
                        sucursales[iMarcadorActual].id,
                        function (data) {
                            var sede = new App.Model.SedeModel(data);
                            console.log('turno sucursal: ' + JSON.stringify(data) + JSON.stringify(sede));
                            $('.turno').empty();
                            $('.turno').append('<p>' + sede.getDisplay('turno') + '</p>');
                        },
                        function (data) {

                            console.log('Error en el refrescar info sucursal: ' + JSON.stringify(data));
                        }
                );

                var divPaso2 = document.getElementById("main");
                if (divPaso2.style.display === 'none')
                {
                    divPaso2.style.display = 'block';
                }
                var marcadorActual = iMarcadorActual;
                $('.informacionSucursal').empty();

                $('.infoEspera').empty();
                $('.infoOpcionA').empty();
                $('.informacionSucursal').append('<p>' + sucursales[marcadorActual].nombre + '</p>');
                $('.informacionSucursal').append('<p>' + sucursales[marcadorActual].dire + '</p>');
                $('.informacionSucursal').append('<p>Lunes-Viernes: 7:00am-6:00pm</p>');
                $('.informacionSucursal').append('<p>Sábado: 8:00am-1:00pm</p>');

                $('.infoEspera').append('<p>&#191Tienes tiempo de realizar tu vuelta en este momento? Pide el siguiente turno disponible</p>');
                $('.infoEspera').append('<br>');
                $('.infoEspera').append('<p>Espera estimada de:</p>');
                $('.infoEspera').append('<p style="font-size:30px">' + calcularTiempoEsperaSucursal(marcadorActual) + ' minutos</p>');
                $('.infoEspera').append('<a href="turnos.html"><button class="btbPedirTurno">Pedir Turno</button></a>');
                $('.infoOpcionA').append('<p>Selecciona una hora en la que quieras hacer tu vuelta. Las horas en verde se encuentran disponibles.</p>');
                $('.infoOpcionA').append('<br>');
                refrescarEstadoHorarios(iMarcadorActual);
            }
            ;


            $(".tablaHorario").click(function () {
                if (anteriorHorarioSel != -1)
                {
                    desSeleccionarHorario(anteriorHorarioSel);
                    //$('.horariosDisponiblesDiv').append();
                }
                var horaSel = parseInt($(this).attr('title'));
                if (celdasHorarios[horaSel].estado != 0)
                {
                    //$('.horariosDisponiblesDiv').append(9);
                    $(this).css('background-color', coloresHorario[parseInt(celdasHorarios[horaSel].estado + 3)]);
                }
                anteriorHorarioSel = horaSel;
            });

            $(".tablaHorario").mouseenter(function () {
                var horaSel = parseInt($(this).attr('title'));
                if (celdasHorarios[horaSel].estado != 0)
                {
                    $(this).css('background-color', coloresHorario[parseInt(celdasHorarios[horaSel].estado + 3)]);
                }
            });

            $(".tablaHorario").mouseleave(function () {
                //if (anteriorHorarioSel != -1)
                //{
                //	desSeleccionarHorario(anteriorHorarioSel);
                //$('.horariosDisponiblesDiv').append();
                //}
                var horaSel = parseInt($(this).attr('title'));
                if (celdasHorarios[horaSel].estado != 0)
                {
                    //$('.horariosDisponiblesDiv').append(9);
                    if (horaSel != anteriorHorarioSel)
                    {
                        $(this).css('background-color', coloresHorario[parseInt(celdasHorarios[horaSel].estado)]);
                    }
                }
                //anteriorHorarioSel = horaSel;
            });

            function desSeleccionarHorario(ihorario)
            {
                $("div[title='" + ihorario + "']").css('background-color', coloresHorario[parseInt(celdasHorarios[anteriorHorarioSel].estado)]);
            }
            ;

            function refrescarEstadoHorarios(iEntidad)
            {
                for (var i = 0; i < celdasHorarios.length; i++)
                {
                    celdasHorarios[i].estado = sucursales[iEntidad].horas[i];
                    $("div[title='" + i + "']").css('background-color', coloresHorario[parseInt(celdasHorarios[i].estado)]);
                }
            }
            ;


        },
        verSedes: function () {
            var self = this;
            this.$el.slideUp("fast", function () {
                self.$el.html(self.listTemplate({entidads: self.entidadModelList.models, componentId: self.componentId, showEdit: false, showDelete: true}));
                self.$el.slideDown("fast");
            });
        },
        redirectFrame: function (url) {
            location.href = 'usuario.html';
        }
    });
    return App.Controller.EntidadController;
}); 