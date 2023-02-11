package perosnzio.controller

import perosnzio.exceptions.{BadRequestException, ExceptionHandler}
import zio.*
import zio.http.model.Status
import zio.http.{Request, Response}
import zio.json.*


private[controller] object includes {
    extension[B] (request: Request) {
        def fromJson[A: JsonDecoder](mapping: A => Task[B]): Task[B] = request.body.asString.map(_.fromJson[A]).flatMap {
            case Left(_) => ZIO.fail(BadRequestException("Bad request"))
            case Right(userForm) => ZIO.succeed(userForm)
        }.flatMap(mapping)
    }

    extension[A] (zio: Task[A]) {
        def toResponse(resultMapping: A => Response): URIO[Any, Response] = zio.fold(ExceptionHandler.errorResponse, resultMapping)
        def toResponse(status: Status): URIO[Any, Response] = toResponse(_ => Response(status))
    }

    extension[A: JsonEncoder] (zio: Task[A]) {
        def toJsonResponse: URIO[Any, Response] = zio.map(_.toJson).toResponse(Response.json)
    }
}
