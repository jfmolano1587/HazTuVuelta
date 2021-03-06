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

package co.edu.uniandes.csw.Arquidalgos.haztuvuelta.logic.mock;
import java.util.ArrayList;
import java.util.List;

import co.edu.uniandes.csw.Arquidalgos.haztuvuelta.logic.dto.HazTuVueltaDTO;
import co.edu.uniandes.csw.Arquidalgos.haztuvuelta.logic.api.IHazTuVueltaLogicService;
import co.edu.uniandes.csw.Arquidalgos.haztuvuelta.master.logic.api._IHazTuVueltaMasterLogicService;
import co.edu.uniandes.csw.Arquidalgos.haztuvuelta.master.logic.dto.HazTuVueltaMasterDTO;
import co.edu.uniandes.csw.Arquidalgos.entidad.logic.api.IEntidadLogicService;
import co.edu.uniandes.csw.Arquidalgos.usuario.logic.api.IUsuarioLogicService;
import co.edu.uniandes.csw.Arquidalgos.entidad.logic.dto.EntidadDTO;
import co.edu.uniandes.csw.Arquidalgos.usuario.logic.dto.UsuarioDTO;
import javax.inject.Inject;


public abstract class _HazTuVueltaMasterMockLogicService implements _IHazTuVueltaMasterLogicService {

    protected static ArrayList<HazTuVueltaMasterDTO> hazTuVueltaMasterDtosList = new ArrayList<HazTuVueltaMasterDTO>() ;
    @Inject
    protected IEntidadLogicService entidadPersistance;
    @Inject
    protected IUsuarioLogicService usuarioPersistance;
    @Inject
    protected IHazTuVueltaLogicService hazTuVueltaPersistance;

    public HazTuVueltaMasterDTO createMasterHazTuVuelta(HazTuVueltaMasterDTO hazTuVuelta) {

        hazTuVueltaPersistance.createHazTuVuelta(hazTuVuelta.getHazTuVueltaEntity());
        for (UsuarioDTO dto : hazTuVuelta.getCreateusuariosHtv()) {
            usuarioPersistance.createUsuario(dto);
        }
        for (EntidadDTO dto : hazTuVuelta.getCreatetiendaOP()) {
            entidadPersistance.createEntidad(dto);
        }
        hazTuVueltaMasterDtosList.add(hazTuVuelta);
        return hazTuVuelta;
    }

    public HazTuVueltaMasterDTO getMasterHazTuVuelta(Long id) {
        for (HazTuVueltaMasterDTO hazTuVueltaMasterDTO : hazTuVueltaMasterDtosList) {
            if (hazTuVueltaMasterDTO.getHazTuVueltaEntity().getId() == id) {
                return hazTuVueltaMasterDTO;
            }
        }

        return null;
    }

    public void deleteMasterHazTuVuelta(Long id) {
        for (HazTuVueltaMasterDTO hazTuVueltaMasterDTO : hazTuVueltaMasterDtosList) {
            if (hazTuVueltaMasterDTO.getHazTuVueltaEntity().getId() == id) {

                for (UsuarioDTO dto : hazTuVueltaMasterDTO.getCreateusuariosHtv()) {
                    usuarioPersistance.deleteUsuario(dto.getId());
                }
                hazTuVueltaPersistance.deleteHazTuVuelta(hazTuVueltaMasterDTO.getId());
                hazTuVueltaMasterDtosList.remove(hazTuVueltaMasterDTO);
                for (EntidadDTO dto : hazTuVueltaMasterDTO.getCreatetiendaOP()) {
                    entidadPersistance.deleteEntidad(dto.getId());
                }
                hazTuVueltaPersistance.deleteHazTuVuelta(hazTuVueltaMasterDTO.getId());
                hazTuVueltaMasterDtosList.remove(hazTuVueltaMasterDTO);
            }
        }

    }

    public void updateMasterHazTuVuelta(HazTuVueltaMasterDTO hazTuVuelta) {

        // update Usuario
        if (hazTuVuelta.getUpdateusuariosHtv() != null) {
            for (UsuarioDTO dto : hazTuVuelta.getUpdateusuariosHtv()) {
                usuarioPersistance.updateUsuario(dto);
            }
        }
        // persist new Usuario
        if (hazTuVuelta.getCreateusuariosHtv() != null) {
            for (UsuarioDTO dto : hazTuVuelta.getCreateusuariosHtv()) {
                UsuarioDTO persistedUsuarioDTO = usuarioPersistance.createUsuario(dto);
                dto = persistedUsuarioDTO;
            }
        }
        // delete Usuario
        if (hazTuVuelta.getDeleteusuariosHtv() != null) {
            for (UsuarioDTO dto : hazTuVuelta.getDeleteusuariosHtv()) {

                usuarioPersistance.deleteUsuario(dto.getId());
            }
        }
        // update Entidad
        if (hazTuVuelta.getUpdatetiendaOP() != null) {
            for (EntidadDTO dto : hazTuVuelta.getUpdatetiendaOP()) {
                entidadPersistance.updateEntidad(dto);
            }
        }
        // persist new Entidad
        if (hazTuVuelta.getCreatetiendaOP() != null) {
            for (EntidadDTO dto : hazTuVuelta.getCreatetiendaOP()) {
                EntidadDTO persistedEntidadDTO = entidadPersistance.createEntidad(dto);
                dto = persistedEntidadDTO;
            }
        }
        // delete Entidad
        if (hazTuVuelta.getDeletetiendaOP() != null) {
            for (EntidadDTO dto : hazTuVuelta.getDeletetiendaOP()) {

                entidadPersistance.deleteEntidad(dto.getId());
            }
        }
    }
}