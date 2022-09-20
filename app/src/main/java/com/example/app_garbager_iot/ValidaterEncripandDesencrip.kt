package com.example.app_garbager_iot

import android.util.Base64
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class ValidaterEncripandDesencrip {
    @Throws(Exception::class)
    private fun desencriptar(datos: String, password: String): String? {
        val secretKey = generateKey(password)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.DECRYPT_MODE, secretKey)
        val datosDescoficados =
            Base64.decode(datos, Base64.DEFAULT)
        val datosDesencriptadosByte = cipher.doFinal(datosDescoficados)
        return String(datosDesencriptadosByte)
    }

    @Throws(Exception::class)
    private fun encriptar(datos: String, password: String): String? {
        val secretKey = generateKey(password)
        val cipher = Cipher.getInstance("AES")
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
        val datosEncriptadosBytes = cipher.doFinal(datos.toByteArray())
        return Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    private fun generateKey(password: String): SecretKeySpec {
        val sha: MessageDigest = MessageDigest.getInstance("SHA-256")
        var key = password.toByteArray(charset("UTF-8"))
        key = sha.digest(key)
        return SecretKeySpec(key, "AES")
    }
}