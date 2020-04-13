# DEMO JAVA JSOUP

## Introducción 
___
El código del respositorio contiene un proyecto Spring Boot con Maven en el que se expone un único servicio REST para la extracción de información de una web mediante la librería JSOUP.  
Se trata de un ejemplo básico de web scrapping en el que se emplean parte de sus elementos clave como Connection, Document y Element, así como la interfaz select que nos proporcionan para realizar filtrados.

## API del servicio 
___
### DESCRIPCIÓN

> Servicio que consulta los anuncios de comisiones de servicio.   
> Se realizará una notificación por correo con los resultados si la búsqueda produce resultados y se informa el destinatario.

### URL
> **/jsoup/xunta/commission**

### MÉTODO
> **GET**

### CÓDIGOS DE ESTADO

> | Código | Descripción |
> | ------ | ----------- |
> |  200   | Búsqueda realizada correctamente. |
> |  400   | Parámetros de entrada incorrectos. |
> |  500   | Error interno de la aplicación. |

### PARÁMETROS DE ENTRADA
> Los parámetros de la petición son los siguientes:
> + **recipient:** destinatario de la notificación por correo (optativo).
> + **finalUsers:** filtro "Abierto a" (optativo).
>   - **F:** funcionariado.
>   - **L:** laboral.
> + **counselling:** filtro "Consellería" (optativo).
> + **startDate:** filtro "Admisión de solicitudes desde". Formato 'yyyy-MM-dd (obligatorio).
> + **endDate:** filtro "Ata". Formato 'yyyy-MM-dd (obligatorio).
> + **group:** filtro "Grupo". Formato 'A1|A2|C1|C2|AP|I|II|III|IV|V' (optativo).
> + **level:** filtro "Nivel" (optativo).
> + **provinceCode:** filtro "Provincia" (optativo).
>   - **15:** A Coruña.
>   - **27:** Lugo.
>   - **28:** Madrid.
>   - **32:** Ourense.
>   - **36:** Pontevedra.
> + **district:** filtro "Concello" (optativo).
>   - **001:** Municipio de A Coruña (para provincia 15 A Coruña).
>   - **Otros:** Obtenibles a partir de:   
      http://www.xunta.es/comisions/AjaxConsultarConcellos.do?id=XX&collection=concellos   
      Siendo XX el código de provincia.
> + **description:** filtro "Descrición" (optativo).

### FORMATO DE SALIDA
> El servicio devuelve un array de objetos de tipo CommissionResult, con los siguientes campos:
> + **destinyCenter:** Centro de destino.
> + **jobCode:** Identificador del puesto.
> + **denomination:** Denominación del puesto.
> + **provision:** Provisión.
> + **group:** Grupo/s.
> + **level:** Código de nivel.

``` json
[
	{
		"destinyCenter": "SERVIZO XURÍDICO-ADMINISTRATIVO (A CORUÑA)",
		"jobCode": "MRC991020115001005",
		"denomination": "XEFATURA SECCIÓN XURÍDICA",
		"provision": "C",
		"group": "A1,A2",
		"level": "25"
	}
]
```

## Comandos de Maven
```bash
# Limpia los archivos generados por la compilación anterior, compila, ejecuta los test unitarios e instala el paquete en el respositorio local.
mvn clean install
# Limpia los archivos generados por la compilación anterior, compila, omite la ejecución de los test unitarios e instala el paquete en el respositorio local.
mvn clean install -DskipTests
# Arrancar la aplicación
mvn clean spring-boot:run
```
