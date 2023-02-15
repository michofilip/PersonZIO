package perosnzio.controller

import perosnzio.form.PersonForm
import perosnzio.service.PersonService
import zio.*
import zio.http.*
import zio.http.model.{Method, Status}
import zio.json.*

case class PersonController(private val personService: PersonService)
    extends Controller {

    def routes = Http.collectZIO[Request] {
        case Method.GET -> !! / "people" => personService.findAll.toJsonResponse

        case Method.GET -> !! / "people" / "stream" => personService.findAllStream.toJsonResponse

        case Method.GET -> !! / "people" / id => personService.findById(id.toInt).toJsonResponse

        case request@Method.POST -> !! / "people" => request.fromJson[PersonForm](personService.create).toJsonResponse

        case request@Method.PUT -> !! / "people" / id => request.fromJson[PersonForm](personService.update(id.toInt, _)).toJsonResponse

        case Method.DELETE -> !! / "people" / id => personService.delete(id.toInt).toResponse(Status.NoContent)
    }
}

object PersonController {
    lazy val layer = ZLayer.fromFunction(apply _)
}
