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

package co.edu.uniandes.csw.Arquidalgos.turno.persistence.entity;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.MappedSuperclass;
import static javax.persistence.TemporalType.DATE;

@MappedSuperclass
public abstract class _TurnoEntity {

	@Id
	@GeneratedValue(generator = "Turno")
	private Long id;
	private String name;
	private Integer turno;
	private Long sedeturnoId;
        @Temporal(DATE)
        private Date horaInicio;
        @Temporal(DATE)
        private Date horaFinal;
        @Temporal(DATE)
        private Date fechaTurno;

        public Date getHoraInicio() {
            return horaInicio;
        }

        public void setHoraInicio(Date horaInicio) {
            this.horaInicio = horaInicio;
        }

        public Date getHoraFinal() {
            return horaFinal;
        }

        public void setHoraFinal(Date horaFinal) {
            this.horaFinal = horaFinal;
        }

        public Date getFechaTurno() {
            return fechaTurno;
        }

        public void setFechaTurno(Date fechaTurno) {
            this.fechaTurno = fechaTurno;
        }

        
        
	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	public Integer getTurno(){
		return turno;
	}
	
	public void setTurno(Integer turno){
		this.turno = turno;
	}
	public Long getSedeturnoId(){
		return sedeturnoId;
	}
	
	public void setSedeturnoId(Long sedeturnoId){
		this.sedeturnoId = sedeturnoId;
	}
}