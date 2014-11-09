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
define(['model/entidadModel', 'model/sedeModel', 'model/citaModel'], function(entidadModel) {
    App.Controller._EntidadController = Backbone.View.extend({
        initialize: function(options) {
            this.modelClass = options.modelClass;
            this.listModelClass = options.listModelClass;
            this.showEdit = true;
            this.showDelete = true;
            this.editTemplate = _.template($('#entidad').html());
            this.listTemplate = _.template($('#entidadList').html());
            if (!options || !options.componentId) {
                this.componentId = _.random(0, 100) + "";
            }else{
				this.componentId = options.componentId;
		    }
            var self = this;
            if(self.postInit){
            	self.postInit(options);
            }
        },
        create: function() {
            if (App.Utils.eventExists(this.componentId + '-' +'instead-entidad-create')) {
                Backbone.trigger(this.componentId + '-' + 'instead-entidad-create', {view: this});
            } else {
                Backbone.trigger(this.componentId + '-' + 'pre-entidad-create', {view: this});
                this.currentEntidadModel = new this.modelClass({componentId: this.componentId});
                this._renderEdit();
                Backbone.trigger(this.componentId + '-' + 'post-entidad-create', {view: this});
            }
        },
        list: function(params) {
            if (params) {
                var data = params.data;
            }
            if (App.Utils.eventExists(this.componentId + '-' +'instead-entidad-list')) {
                Backbone.trigger(this.componentId + '-' + 'instead-entidad-list', {view: this, data: data});
            } else {
                Backbone.trigger(this.componentId + '-' + 'pre-entidad-list', {view: this, data: data});
                var self = this;
				if(!this.entidadModelList){
                 this.entidadModelList = new this.listModelClass();
				}
                this.entidadModelList.fetch({
                    data: data,
                    success: function() {
                        self._renderList();
                        Backbone.trigger(self.componentId + '-' + 'post-entidad-list', {view: self});
                    },
                    error: function(mode, error) {
                        Backbone.trigger(self.componentId + '-' + 'error', {event: 'entidad-list', view: self, error: error});
                    }
                });
            }
        },
        edit: function(params) {
            var id = params.id;
            var data = params.data;
            if (App.Utils.eventExists(this.componentId + '-' +'instead-entidad-edit')) {
                Backbone.trigger(this.componentId + '-' + 'instead-entidad-edit', {view: this, id: id, data: data});
            } else {
                Backbone.trigger(this.componentId + '-' + 'pre-entidad-edit', {view: this, id: id, data: data});
                if (this.entidadModelList) {
                    this.currentEntidadModel = this.entidadModelList.get(id);
                    this.currentEntidadModel.set('componentId',this.componentId); 
                    this._renderEdit();
                    Backbone.trigger(this.componentId + '-' + 'post-entidad-edit', {view: this, id: id, data: data});
                } else {
                    var self = this;
                    this.currentEntidadModel = new this.modelClass({id: id});
                    this.currentEntidadModel.fetch({
                        data: data,
                        success: function() {
                            self.currentEntidadModel.set('componentId',self.componentId); 
                            self._renderEdit();
                            Backbone.trigger(self.componentId + '-' + 'post-entidad-edit', {view: this, id: id, data: data});
                        },
                        error: function() {
                            Backbone.trigger(self.componentId + '-' + 'error', {event: 'entidad-edit', view: self, id: id, data: data, error: error});
                        }
                    });
                }
            }
        },
        destroy: function(params) {
            var id = params.id;
            var self = this;
            if (App.Utils.eventExists(this.componentId + '-' +'instead-entidad-delete')) {
                Backbone.trigger(this.componentId + '-' + 'instead-entidad-delete', {view: this, id: id});
            } else {
                Backbone.trigger(this.componentId + '-' + 'pre-entidad-delete', {view: this, id: id});
                var deleteModel;
                if (this.entidadModelList) {
                    deleteModel = this.entidadModelList.get(id);
                } else {
                    deleteModel = new this.modelClass({id: id});
                }
                deleteModel.destroy({
                    success: function() {
                        Backbone.trigger(self.componentId + '-' + 'post-entidad-delete', {view: self, model: deleteModel});
                    },
                    error: function() {
                        Backbone.trigger(self.componentId + '-' + 'error', {event: 'entidad-delete', view: self, error: error});
                    }
                });
            }
        },
        save: function() {
            var self = this;
            var model = $('#' + this.componentId + '-entidadForm').serializeObject();
            if (App.Utils.eventExists(this.componentId + '-' +'instead-entidad-save')) {
                Backbone.trigger(this.componentId + '-' + 'instead-entidad-save', {view: this, model : model});
            } else {
                Backbone.trigger(this.componentId + '-' + 'pre-entidad-save', {view: this, model : model});
                this.currentEntidadModel.set(model);
                this.currentEntidadModel.save({},
                        {
                            success: function(model) {
                                Backbone.trigger(self.componentId + '-' + 'post-entidad-save', {model: self.currentEntidadModel});
                            },
                            error: function(error) {
                                Backbone.trigger(self.componentId + '-' + 'error', {event: 'entidad-save', view: self, error: error});
                            }
                        });
            }
        },
        _renderList: function() {
            var self = this;
            this.$el.slideUp("fast", function() {
                self.$el.html(self.listTemplate({entidads: self.entidadModelList.models, componentId: self.componentId, showEdit : true , showDelete : false}));
                self.$el.slideDown("fast");
            });
        },
        _renderEdit: function() {
            var self = this;
            this.$el.slideUp("fast", function() {
                self.$el.html(self.editTemplate({entidad: self.currentEntidadModel, componentId: self.componentId , showEdit : self.showEdit , showDelete : self.showDelete
 
				}));
                self.$el.slideDown("fast");
            });
        }
    });
    return App.Controller._EntidadController;
});