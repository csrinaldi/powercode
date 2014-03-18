Powercode
=========

Power code es un repositorio de aplicaciones funcionales para diferentes componentes utilizables en aplicaciones empresariales:

#Akka-Process (Alpha)
Este componente utiliza el framework Akka para gestionar procesos concurrentes, haciendo uso de una cola de mensajes rabbitMQ.
Los mensajes estan codificados haciendo uso de ProtoBuf de Google.
Para la Inyección de Dependencias se utiliza CDI.

#Akka-Console (Alpha)
Este componente es una terminal remota para administracion de los procesos gestionados por la plataforma Akka-Process, permite conecciòn remota a la plataforma y la ejecucion de diferentes procesos.
