package models.dtos

import helpers.TestsCommon

class CredentialsTest extends TestsCommon{
  "Credentials companion object" when {
    "validPassword" should {
      "treat minimal valid password as valid" in {
        Credentials.validPassword("Test1234!") mustBe true
      }

      "treat strong password as valid" in {
        Credentials.validPassword("H@rdPas$w0rd1234_0") mustBe true
      }

      "treat password as invalid if contains no digit" in {
        Credentials.validPassword("Testtest!") mustBe false
      }

      "treat password as invalid if contains no uppercase" in {
        Credentials.validPassword("test1234!") mustBe false
      }

      "treat password as invalid if contains no lowercase" in {
        Credentials.validPassword("TEST1234!") mustBe false
      }

      "treat password as invalid if contains no symbol" in {
        Credentials.validPassword("Test1234") mustBe false
      }

      "treat password as invalid if less then 8 chars" in {
        Credentials.validPassword("Test12!") mustBe false
      }
    }
  }
}
