package ejemplosSim.json

import net.liftweb.json._
import net.liftweb.json.Serialization.{read, write}

object json_samples {
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet
  
  implicit val formats = DefaultFormats // Brings in default date formats etc.
                                                  //> formats  : net.liftweb.json.DefaultFormats.type = net.liftweb.json.DefaultFo
                                                  //| rmats$@42d3bd8b
  
  //De JSON a objetos
  val json = parse("""
         { "name": "joe",
           "address": {
             "street": "Bulevard",
             "city": "Helsinki"
           },
           "children": [
             {
               "name": "Mary",
               "age": 5
               "birthdate": "2004-09-04T18:06:22Z"
             },
             {
               "name": "Mazy",
               "age": 3
             }
           ]
         }
       """)                                       //> json  : net.liftweb.json.JValue = JObject(List(JField(name,JString(joe)), JF
                                                  //| ield(address,JObject(List(JField(street,JString(Bulevard)), JField(city,JStr
                                                  //| ing(Helsinki))))), JField(children,JArray(List(JObject(List(JField(name,JStr
                                                  //| ing(Mary)), JField(age,JInt(5)), JField(birthdate,JString(2004-09-04T18:06:2
                                                  //| 2Z)))), JObject(List(JField(name,JString(Mazy)), JField(age,JInt(3)))))))))
       
  //Defino cases case (igual que las normales pero para pattern matching) para cargar la estructura anterior
  case class Child(name: String, age: Int, birthdate: Option[java.util.Date])
  case class Address(street: String, city: String)
  case class Person(name: String, address: Address, children: List[Child])
  
  //Deserialización
  val p = json.extract[Person]                    //> p  : ejemplosSim.json.json_samples.Person = Person(joe,Address(Bulevard,Hel
                                                  //| sinki),List(Child(Mary,5,Some(Sat Sep 04 20:06:22 CEST 2004)), Child(Mazy,3
                                                  //| ,None)))
  //Serialización
  val ser = write(p)                              //> ser  : String = {"$outer":{},"name":"joe","address":{"$outer":{},"street":"
                                                  //| Bulevard","city":"Helsinki"},"children":[{"$outer":{},"name":"Mary","age":5
                                                  //| ,"birthdate":"2004-09-04T18:06:22Z"},{"$outer":{},"name":"Mazy","age":3}]}
  
}