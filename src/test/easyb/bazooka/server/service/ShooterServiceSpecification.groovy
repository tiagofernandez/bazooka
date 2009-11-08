import bazooka.server.BazookaContext
import bazooka.server.BazookaContext4Test
import bazooka.server.service.ShooterServiceImpl

final BazookaContext ctx = new BazookaContext4Test()
final ShooterServiceImpl svc = ctx.getInjector().getInstance(ShooterServiceImpl.class)

it "should create a new shooter", {
  def final shooterName = "Hurl"
  def createdShooter = svc.createShooter(shooterName)
  createdShooter.getName().shouldBe shooterName
}