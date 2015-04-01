/**
 * Copyright 2015 eBusiness Information, Groupe Excilys (www.ebusinessinformation.fr)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.gatling.http

import io.gatling.core.body.{ RawFileBodies, ElFileBodies }
import io.gatling.core.config.GatlingConfiguration
import io.gatling.core.result.message.KO
import io.gatling.core.session._
import io.gatling.http.action.{ AddCookieBuilder, CookieDSL }
import io.gatling.http.cache.HttpCaches
import io.gatling.http.check.HttpCheckSupport
import io.gatling.http.check.ws.WsCheckSupport
import io.gatling.http.config.{ DefaultHttpProtocol, HttpProtocolBuilder }
import io.gatling.http.cookie.CookieSupport
import io.gatling.http.feeder.SitemapFeederSupport
import io.gatling.http.request.builder.polling.Polling
import io.gatling.http.request.{ BodyPart, ExtraInfo }
import io.gatling.http.request.builder.Http
import io.gatling.http.request.builder.sse.Sse
import io.gatling.http.request.builder.ws.Ws

trait HttpModule extends HttpCheckSupport with WsCheckSupport with SitemapFeederSupport {

  def http(implicit defaultHttpProtocol: DefaultHttpProtocol) = new HttpProtocolBuilder(defaultHttpProtocol.value)

  val Proxy = io.gatling.http.config.HttpProxyBuilder.apply _

  def http(requestName: Expression[String])(implicit configuration: GatlingConfiguration, httpCaches: HttpCaches) = new Http(requestName)
  def addCookie(cookie: CookieDSL)(implicit defaultHttpProtocol: DefaultHttpProtocol) = new AddCookieBuilder(cookie.name, cookie.value, cookie.domain, cookie.path, cookie.expires.getOrElse(-1L), cookie.maxAge.getOrElse(-1))
  def flushSessionCookies = CookieSupport.FlushSessionCookies
  def flushCookieJar = CookieSupport.FlushCookieJar
  def flushHttpCache(implicit httpCaches: HttpCaches) = httpCaches.FlushCache

  def sse(requestName: Expression[String])(implicit configuration: GatlingConfiguration, defaultHttpProtocol: DefaultHttpProtocol) = new Sse(requestName)
  def sse(requestName: Expression[String], sseName: String)(implicit configuration: GatlingConfiguration, defaultHttpProtocol: DefaultHttpProtocol) = new Sse(requestName, sseName)
  def ws(requestName: Expression[String])(implicit configuration: GatlingConfiguration, defaultHttpProtocol: DefaultHttpProtocol) = new Ws(requestName)
  def ws(requestName: Expression[String], wsName: String)(implicit configuration: GatlingConfiguration, defaultHttpProtocol: DefaultHttpProtocol) = new Ws(requestName, wsName)
  def polling(implicit configuration: GatlingConfiguration, defaultHttpProtocol: DefaultHttpProtocol) = new Polling()
  def polling(pollingName: String)(implicit configuration: GatlingConfiguration, defaultHttpProtocol: DefaultHttpProtocol) = new Polling(pollingName)

  val HttpHeaderNames = HeaderNames
  val HttpHeaderValues = HeaderValues

  val dumpSessionOnFailure: ExtraInfo => List[Any] = extraInfo => extraInfo.status match {
    case KO => List(extraInfo.session)
    case _  => Nil
  }

  def Cookie = CookieDSL

  def ElFileBodyPart(filePath: Expression[String])(implicit configuration: GatlingConfiguration, elFileBodies: ElFileBodies): BodyPart =
    BodyPart.elFileBodyPart(None, filePath)
  def ElFileBodyPart(name: Expression[String], filePath: Expression[String])(implicit configuration: GatlingConfiguration, elFileBodies: ElFileBodies): BodyPart =
    BodyPart.elFileBodyPart(Some(name), filePath)

  def StringBodyPart(string: Expression[String])(implicit configuration: GatlingConfiguration): BodyPart =
    BodyPart.stringBodyPart(None, string)
  def StringBodyPart(name: Expression[String], string: Expression[String])(implicit configuration: GatlingConfiguration): BodyPart =
    BodyPart.stringBodyPart(Some(name), string)

  def RawFileBodyPart(filePath: Expression[String])(implicit configuration: GatlingConfiguration, rawFileBodies: RawFileBodies): BodyPart =
    BodyPart.rawFileBodyPart(None, filePath)
  def RawFileBodyPart(name: Expression[String], filePath: Expression[String])(implicit configuration: GatlingConfiguration, rawFileBodies: RawFileBodies): BodyPart =
    BodyPart.rawFileBodyPart(Some(name), filePath)

  def ByteArrayBodyPart(bytes: Expression[Array[Byte]]): BodyPart = BodyPart.byteArrayBodyPart(None, bytes)
  def ByteArrayBodyPart(name: Expression[String], bytes: Expression[Array[Byte]]): BodyPart = BodyPart.byteArrayBodyPart(Some(name), bytes)
}
