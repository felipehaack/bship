package api

import play.api.mvc.Action
import javax.inject.{Inject, Singleton}

import models.Fire
import models.Player._

import services.MatchService

@Singleton
class MatchApi @Inject()(matchService: MatchService) extends Api {

  private def isValidChar(char: Char): Boolean = {

    char match {
      case c if c >= 48 && c <= 57 || c >= 65 && c <= 70 => true
      case _ => false
    }
  }

  private def isValidSalvos(salvos: Array[String]): Boolean = {

    val result = for {
      i <- salvos.indices
      if salvos(i).length == 3 && isValidChar(salvos(i)(0)) && isValidChar(salvos(i)(2)) && salvos(i)(1) == 'x'
    } yield i

    result.length match {
      case size if salvos.length == size => true
      case _ => false
    }
  }

  def fire(gameId: Int) = Action(json[Fire.Create]) { implicit request =>

    val fire = request.body

    val result = isValidSalvos(fire.salvo)

    result match {

      case true => matchService.verifyPlayerTurn(gameId, Turn.Player) match {

        case true =>

          val result = matchService.damage(gameId, fire, Turn.Player)

          Ok.asJson(result)

        case false => BadRequest
      }

      case _ => BadRequest
    }
  }

  def fired(gameId: Int) = Action(json[Fire.Create]) { implicit request =>

    val fire = request.body

    val result = isValidSalvos(fire.salvo)

    result match {

      case true => matchService.verifyPlayerTurn(gameId, Turn.Enemy) match {

        case true =>

          val result = matchService.damage(gameId, fire, Turn.Enemy)

          Ok.asJson(result)

        case false => BadRequest
      }

      case _ => BadRequest
    }
  }
}
