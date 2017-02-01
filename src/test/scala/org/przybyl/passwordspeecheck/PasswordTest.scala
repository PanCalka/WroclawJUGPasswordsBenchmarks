package org.przybyl.passwordspeecheck

import org.apache.commons.codec.digest.Crypt
import org.mindrot.jbcrypt.BCrypt
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

class PasswordTest extends FlatSpec with Matchers with BeforeAndAfterEach {

  "BCrypt" should "hash simple ASCII password" in {
    val pass = "password"
    val hashed = BCrypt.hashpw(pass, BCrypt.gensalt(12))
    BCrypt.checkpw(pass, hashed) shouldBe true
  }

  it should "hash UTF-8 password" in {
    val pass = "gęśla_jaźń"
    val hashed = BCrypt.hashpw(pass, BCrypt.gensalt(12))
    BCrypt.checkpw(pass, hashed) shouldBe true
  }

  it should "check password" in {
    val pass = "test"
    val hashed = "$2a$12$Zn7RvqTWykTIHKSfzG0UgObSjH1rmK/1DhKvWImaFMrx5oyqrxOx2"
    BCrypt.checkpw(pass, hashed) shouldBe true
    BCrypt.checkpw("fail", hashed) shouldBe false
  }

  it should "check password from PHP" in {
    val pass = "gęśla_jaźń"
    val hashed = "$2y$12$jq0/bRZph07QsioanDNXVe9NoVg1C3LCna7L/uTw5zasNANvZ1TEe".replaceFirst("""\$2y\$""", """\$2a\$""")
    BCrypt.checkpw(pass, hashed) shouldBe true
  }

  "Crypt" should "work with SHA512-CRYPT" in {
    val sha512Hash = Crypt.crypt("gęśla_jaźń", null)

    val hashJazn = Crypt.crypt("gęśla_jaźń", sha512Hash)
    hashJazn shouldEqual sha512Hash

    val hashMaz = Crypt.crypt("gęśla_maź", sha512Hash)
    hashMaz should not equal sha512Hash
  }

  it should "complain about incorrect salt" in {
    val externalHash = "$6$$Y8Et6wWDdXO2tJZRabvSfQvG2Lc8bAS6D9COIsMXEJ2KjA27wqDuAyd/CdazBQc3H3xQX.JXMKxJeRz2OqTkl."
    val thrown = intercept[IllegalArgumentException] {
      Crypt.crypt("opnsense", externalHash)
    }
    thrown.getMessage should startWith("Invalid salt value")
  }

  it should "check external SHA512-CRYPT password hashes with default rounds" in {
    val externalHash = "$6$s.yxoQr0TOaB1eSX$osSY92AAVL0p1HjChx8bqKLx/akgyYgqahKk9.EI57FB3zuIjjVBqSjpnaV8fDXLP.t.OMX6twbcz9J1RJRXC1"
    val localHash = Crypt.crypt("WrocławJUG", externalHash)
    localHash shouldEqual externalHash
  }
  it should "check external SHA512-CRYPT password hashes with custom rounds" in {
    val externalHash = "$6$rounds=100000$Ys67wLHkcq8D0rSq$zLgLotg5qg3u6yjXQ1F40lYJadbhjaR82eE54YpdMgTWl9HWFyd2X2Jnqh62eixmjQ1sXHngAcyThCBL8PHWd0"
    val localHash = Crypt.crypt("WrocławJUG", externalHash)
    localHash shouldEqual externalHash
  }


}
