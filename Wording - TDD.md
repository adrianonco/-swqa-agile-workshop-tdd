# Taller de TDD

Queremos desarrollar una nueva funcionalidad consistente en enviar un correo electrónico a un profesor a partir de su id.

1. El driver port `CampusApp` debería disponer de un nuevo método para esta funcionalidad que reciba el `id` del profesor, el asunto y el cuerpo del mensaje.
2. Si no existe un usuario con dicho `id` se informará mediante una excepción que contenga el mensaje `"User 2466 does not exist"` (dónde `2466` es el `id` del usuario indicado en la petición).
3. Si existe un usuario con dicho `id` pero no es un profesor, se informará mediante una excepción que contenga el mensaje `"User 2466 is not a teacher"` (dónde `2466` es el `id` del usuario indicado en la petición).
4. En caso contrario, se recuperará el `email` del usuario de la base de datos y se le enviará un correo con el asunto y el cuerpo indicados.

## Requisitos no funcionales:

- Cualquier línea de código que se cambie o añada deberá estar cubierta por las pruebas ya existentes o por nuevas pruebas.
- Para centrarnos en TDD, en esta práctica se usarán las estrategias implementadas por `CampusAppEndToEndTest` y `PostgreSqlUsersRepositoryTest` como puntos de partida. Si queréis usar otra estrategia de testing de las explicadas debéis consultar antes con el profesor.

## Mejora 1

Después de unas pruebas de usabilidad nos indican que sucede a menudo que el usuario olvida indicar el asunto del mensaje. Queremos implementar esta modificación:

El método de envío de un email a un profesor comprobará que el asunto del mensaje no sea `null`, ni un `String` vacío ni un `String` con solo carácteres en blanco. Si tal fuera el caso, se indicará con un mensaje de error `"The email subject is mandatory"`.


## Mejora 2

Desde el análisis de la usabilidad nos dicen que, de nuevo, han detectado una posible mejora. Aunque sea opcional, en ocasiones el usuario olvida indicar el cuerpo del mensaje. Queremos implementar esta modificación:

1. Añadiremos un parámetro booleano llamado `confirm` (confirmar).

2. Si el parámetro `confirm` es `false` y el cuerpo del mensaje es `null`, se informará mediante una excepción que contenga el mensaje `"No se ha indicado el cuerpo del mensaje. Infórmelo o marque la casilla 'Confirmar'"`. Si el parámetro `confirm` es `true` se enviará el mensaje sin cuerpo. Para evitar confusiones, si el cuerpo del mensaje a enviar es un `String` vacío o un `String` con solo carácteres en blanco, se informará como error con un mensaje que distinga entre ambos casos, independientemente del valor del parámetro `confirm`; es decir, si no se quiere enviar cuerpo del mensaje, deberá indicarse con `null` Y con el parámetro `confirm` a `true`.
