package demo

import akka.actor.{ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import com.typesafe.config.Config

class SettingsImpl(config: Config) extends Extension {

  lazy val SERVER_URL: String = config.getString("demo.server.url")
}

object Settings extends ExtensionId[SettingsImpl] with ExtensionIdProvider {

  override def createExtension(system: ExtendedActorSystem): SettingsImpl = new SettingsImpl(system.settings.config)

  override def lookup() = Settings
}