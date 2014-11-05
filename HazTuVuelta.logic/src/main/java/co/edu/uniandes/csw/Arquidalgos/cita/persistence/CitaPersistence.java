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

package co.edu.uniandes.csw.Arquidalgos.cita.persistence;

import co.edu.uniandes.csw.Arquidalgos.cita.logic.dto.CitaDTO;
import javax.ejb.Stateless;
import javax.enterprise.inject.Default;

import co.edu.uniandes.csw.Arquidalgos.cita.persistence.api.ICitaPersistence;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.ejb.LocalBean;
import utilidadesHTV.ConstantesYMetodos;
import utilidadesHTV.Tiempo;

@Default
@Stateless 
@LocalBean
public class CitaPersistence extends _CitaPersistence  implements ICitaPersistence {

    public List<CitaDTO> darCitasRango (Date fecha){
        
        List<CitaDTO> citas = getCitas();
        List<CitaDTO> resp = new ArrayList<CitaDTO>();
        for (CitaDTO cita : citas) {
            
            if ( fecha.before(cita.getHoraIni())&&fecha.after(cita.getHoraFin())&&cita.isEspera()){
                
                
                resp.add(cita);
            }
            
        }
        
        return resp;
    }
    
    
    public List<CitaDTO> darCitasEsperaHora ( Date fecha){
        
        Calendar c1 = new GregorianCalendar();
        c1.setTime(fecha);
        List <CitaDTO> resp = new ArrayList<CitaDTO>();
        
        List<CitaDTO> citasHoy = darCitasHoy();
        
        for (CitaDTO citaActual : citasHoy) {
            
            Calendar c = new GregorianCalendar();
            c.setTime(citaActual.getHoraIni());
            if ( c.get(Calendar.HOUR_OF_DAY) == c1.get(Calendar.HOUR_OF_DAY) &&
                    c.get(Calendar.MINUTE)==c1.get(Calendar.MINUTE)){
                
                resp.add(citaActual);
            }
            
        }
        
        return resp;
    }

    public List <CitaDTO> darCitasHoy (){
        
        List <CitaDTO> resp = new ArrayList<CitaDTO>();
               
        List<CitaDTO> citas = getCitas();
        
        for (CitaDTO cita : citas) {
            
            
            if ( ConstantesYMetodos.citasMismoDia(Tiempo.getCurrentDate(), cita.getFechaCita())){
                                
                resp.add(cita);
            }
            
        }
        
        return resp;
    }
}