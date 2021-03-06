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

package co.edu.uniandes.csw.Arquidalgos.haztuvuelta.master.logic.dto;

import co.edu.uniandes.csw.Arquidalgos.usuario.logic.dto.UsuarioDTO;
import co.edu.uniandes.csw.Arquidalgos.entidad.logic.dto.EntidadDTO;
import co.edu.uniandes.csw.Arquidalgos.haztuvuelta.logic.dto.HazTuVueltaDTO;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public abstract class _HazTuVueltaMasterDTO {

 
    protected HazTuVueltaDTO haztuvueltaEntity;
    protected Long id;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public HazTuVueltaDTO getHazTuVueltaEntity() {
        return haztuvueltaEntity;
    }

    public void setHazTuVueltaEntity(HazTuVueltaDTO haztuvueltaEntity) {
        this.haztuvueltaEntity = haztuvueltaEntity;
    }
    
    public List<UsuarioDTO> createusuariosHtv;
    public List<UsuarioDTO> updateusuariosHtv;
    public List<UsuarioDTO> deleteusuariosHtv;
    public List<UsuarioDTO> listusuariosHtv;	
    public List<EntidadDTO> createtiendaOP;
    public List<EntidadDTO> updatetiendaOP;
    public List<EntidadDTO> deletetiendaOP;
    public List<EntidadDTO> listtiendaOP;	
	
	
	
    public List<UsuarioDTO> getCreateusuariosHtv(){ return createusuariosHtv; };
    public void setCreateusuariosHtv(List<UsuarioDTO> createusuariosHtv){ this.createusuariosHtv=createusuariosHtv; };
    public List<UsuarioDTO> getUpdateusuariosHtv(){ return updateusuariosHtv; };
    public void setUpdateusuariosHtv(List<UsuarioDTO> updateusuariosHtv){ this.updateusuariosHtv=updateusuariosHtv; };
    public List<UsuarioDTO> getDeleteusuariosHtv(){ return deleteusuariosHtv; };
    public void setDeleteusuariosHtv(List<UsuarioDTO> deleteusuariosHtv){ this.deleteusuariosHtv=deleteusuariosHtv; };
    public List<UsuarioDTO> getListusuariosHtv(){ return listusuariosHtv; };
    public void setListusuariosHtv(List<UsuarioDTO> listusuariosHtv){ this.listusuariosHtv=listusuariosHtv; };	
    public List<EntidadDTO> getCreatetiendaOP(){ return createtiendaOP; };
    public void setCreatetiendaOP(List<EntidadDTO> createtiendaOP){ this.createtiendaOP=createtiendaOP; };
    public List<EntidadDTO> getUpdatetiendaOP(){ return updatetiendaOP; };
    public void setUpdatetiendaOP(List<EntidadDTO> updatetiendaOP){ this.updatetiendaOP=updatetiendaOP; };
    public List<EntidadDTO> getDeletetiendaOP(){ return deletetiendaOP; };
    public void setDeletetiendaOP(List<EntidadDTO> deletetiendaOP){ this.deletetiendaOP=deletetiendaOP; };
    public List<EntidadDTO> getListtiendaOP(){ return listtiendaOP; };
    public void setListtiendaOP(List<EntidadDTO> listtiendaOP){ this.listtiendaOP=listtiendaOP; };	
	
	
}

