package com.uca.capas.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

//enum de rangos

 enum rangos{
	inferior (1),
	maximo_25(25),
	 maximo_100(100);
	 private int rango;
	rangos(int rango) {this.rango=rango;}
	public int getrango() {return rango;}
}
 enum datos{
	nombre ("El nombre debe tener entre 1 y 25 caracteres."),
	apellido("El apellido debe tener entre 1 y 25 caracteres."),
	lugar_nacimiento("El lugar de nacimiento debe tener entre 1 y 25 caracteres."),
	colegio("El instituto o colegio de procedencia debe tener entre 1 y 100 caracteres."),
	numero_8("El numero debe tener exactamente 8 dígitos."),
	numero_valido("Ingrese un numero válido."),
	nacimiento_fecha("La fecha minima permitida es el 1 de enero de 2003"),
	nacimiento_invalido("Debe ingresar una fecha valida.")
	;

	
	 private String datos;
	 datos(String datos) {this.datos=datos;}
	public String getdatos() {return datos;}
 };
	

@Controller
public class MainController 
{
	
	@RequestMapping("/ingresar")
	public String inicio() 
	{
		String paginainicial="inicio";
		return paginainicial;
	}
	
	@RequestMapping("/validar")
	
	public ModelAndView valido(
			@RequestParam(value="nombre") String nombre, 
			@RequestParam(value="apellido") String apellido,
			@RequestParam(value="fechan") String fecha,
			@RequestParam(value="lugarn") String lugar_nacimiento,
			@RequestParam(value="colegio") String colegio,
			@RequestParam(value="telefono") String numero,
			@RequestParam(value="celular") String celular) 
	{
		//Lista para guardar Errores
		 List<String> errores = new ArrayList<>();
		 //Validando nombre
		validar_datos(nombre, 1, errores);
		//Validando Apellido
		validar_datos(apellido, 2, errores);
		//validando Fecha Nacimiento
		fecha_nacimiento(fecha, errores);
		//validando lugar de nacimiento
		validar_datos(lugar_nacimiento,3,errores);
		//validando colegio
		validar_datos(colegio, 4, errores);
		//Validando telefono fijo
		numeros_telefono(numero, 1, errores);
		//validando telefono celular
		numeros_telefono(celular, 2, errores);
		
		
		
		ModelAndView resultado = new ModelAndView();
		
		if(!errores.isEmpty()) {
			resultado.addObject("errors", errores);
			resultado.setViewName("resultado_fracaso");
		}else {
			resultado.setViewName("resultado_exito");
		}
		
		return resultado;
	
	}
	
	void validar_datos(String valor,int opcion,List<String> errores)
	{
		rangos r_minimo = rangos.inferior;
		rangos r_maximo_25 = rangos.maximo_25;
		rangos r_maximo_100 = rangos.maximo_100;
		datos d_nombre = datos.nombre;
		datos d_apellido=datos.apellido;
		datos d_lugar_nacimiento=datos.lugar_nacimiento;
		datos d_colegio=datos.colegio;
		if(valor.length() < r_minimo.getrango() || valor.length() > r_maximo_25.getrango() )
		{
			switch(opcion) 
			{
			case 1:
				errores.add(d_nombre.getdatos());
				break;
			case 2:
				errores.add(d_apellido.getdatos());
				break;
			case 3:
				errores.add(d_lugar_nacimiento.getdatos());
				break;
			}
		}
		if(opcion==4)
		{
			if(valor.length() < r_minimo.getrango() || valor.length() > r_maximo_100.getrango() )
			{
				errores.add(d_colegio.getdatos());
			}
		}
	
	}
	void fecha_nacimiento(String fecha,List<String> errores)
	{
		datos fecha_rango=datos.nacimiento_fecha;
		datos fecha_invalida=datos.nacimiento_invalido;
		try {
			Date nacimiento = new SimpleDateFormat("yyyy-MM-dd").parse(fecha);
			Date minDate = new SimpleDateFormat("yyyy-MM-dd").parse("2002-12-31");
			if(!nacimiento.after(minDate)) {
				errores.add(fecha_rango.getdatos());
			}
		} catch (ParseException e) {
				errores.add(fecha_invalida.getdatos());
		}
	}
	void numeros_telefono(String numero,int opcion,List<String> errores)
	{
		datos numero_8=datos.numero_8;
		datos numero_valido=datos.numero_valido;
		try {
			Integer.parseInt(numero);
			if(numero.length() != 8) 
			{
				switch (opcion)
				{
					case 1:
						errores.add("El Numero fijo debe cumplir: "+numero_8.getdatos());
					break;
					case 2:
						errores.add("El Numero de celular debe cumplir: "+numero_8.getdatos());
					break;
				}
			}
		}catch(NumberFormatException e) {
			switch (opcion) 
			{
				case 1:
					errores.add("El Numero fijo debe cumplir: "+numero_valido.getdatos());
					break;
				case 2:
					errores.add("El Numero de celular debe cumplir: "+numero_valido.getdatos());
					break;

			}
			
		}
	}
}