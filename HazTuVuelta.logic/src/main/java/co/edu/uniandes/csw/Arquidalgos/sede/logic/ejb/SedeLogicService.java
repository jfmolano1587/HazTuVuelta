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

package co.edu.uniandes.csw.Arquidalgos.sede.logic.ejb;

import co.edu.uniandes.csw.Arquidalgos.cita.logic.dto.CitaDTO;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless; 
import javax.inject.Inject;

import javax.enterprise.inject.Default;

import co.edu.uniandes.csw.Arquidalgos.sede.logic.api.ISedeLogicService;
import java.util.Date;

@Default
@Stateless
@LocalBean
public class SedeLogicService extends _SedeLogicService implements ISedeLogicService {

    public String aumentarTurno() {
        
        return persistance.aumentarTurno()+"";
    }
    
    public String turnoActual(){
        
        return persistance.turnoActual()+"";
    }

    public int asignarSiguienteTurno(Long idSede, String cedula) throws Exception{
        
        return persistance.asignarSiguienteTurno(idSede,cedula);
    }
    
    public void atenderTurno(Long idSede) {
        
        persistance.atenderTurno(idSede);
    }

    public Integer darUltimoTurnoAsignado(Long idSede) {
        
        return persistance.darUltimoTurnoAsignado(idSede);
    }

    public Date darUltimoInicioDeCita(Long idSede) {
        
        return persistance.darUltimoInicioDeCita(idSede);
    }

    public Integer darUltimoTurnoAtendido(Long idSede) {
        
        return persistance.darUltimoTurnoAtendido(idSede);
    }

    public Date darHoraAproximadaAtencion(String cedula) throws Exception {
       
        return persistance.darHoraAproximadaAtencion(cedula);
    }

    public void reservarCita(CitaDTO nuevaCita) throws Exception {
        
        persistance.reservarCita(nuevaCita, nuevaCita.getName());
    }

}