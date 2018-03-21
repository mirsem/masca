package simulator

import simulator.mason.MasonAgent


/**
 * Esta clase representa tanto a los sensores como a los actuadores del robot. Cualquier sensor o actuador del mismo debe
 * derivar de este trait para agregar funcionalidad al robot. Normalmente un Interactuador tiene una clase "amiga" derivada
 * de entorno, ya que hay que agregar nuevas capacidades al simulador (a partir de Entorno) para poder agregar nuevos sensores
 * o actuadores (que derivarán de Interactuador)
 * @author fidel
 *
 */
trait Interactuator {
  
   //El valor del agente Mason se debe proporcionar mediante una función, no mediante definición de variable, ya que esto acarrea problemas al llamar
   //a super en traits derivados..
  
   /** 
    * Retornar el agente sobre el cual funcionará el Inteacturador. Se trata de una funcion abstracta que tiene que 
    * estar definida en la clase que implemente el trait
    */
   def masonAgent():MasonAgent
   
   /** Inicializador del Interactuador */
   def initInteractuator():Unit = {}
}