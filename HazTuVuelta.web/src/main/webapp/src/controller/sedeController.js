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
define(['controller/_sedeController', 'delegate/sedeDelegate'], function () {
    App.Controller.SedeController = App.Controller._SedeController.extend({
        postInit: function (options) {
            var self = this;
            Backbone.on(this.componentId + '-sede-sigTurno', function (params) {
                self.siguienteTurno(params);
            });

        },
        siguienteTurno: function (params) {
            var self = this;
            this.sedeDelegate = new App.Delegate.SedeDelegate();
            this.sedeDelegate.siguienteTurnoDelagate(
                    params.id,
                    function (data) {
                        $('#turnoAtendido').html( data );
                        self.list();
                        self._renderEdit();
                        console.log('Sin errores se pasó al siguiente turno');
                    },
                    function (data) {
                         $('#turnoAtendido').html( data );
                         self.list();
                         self._renderEdit();
                        console.log('Error al pasar al siguiente turno: ' + JSON.stringify(data));
                    }
            );
        }
    });
    return App.Controller.SedeController;
}); 