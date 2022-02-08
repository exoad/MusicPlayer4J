package backend.audio

import app.CLI.print
import app.global.cli.CliType
import it.sauronsoftware.jave.*
import java.io.File

object Music {
    private fun randomFileName(): String {
        val sb = StringBuilder()
        for (i in 0..15) {
            val n = (Math.random() * 62).toInt()
            if (n < 10) {
                sb.append(n)
            } else if (n < 36) {
                sb.append(('a'.toInt() + n - 10).toChar())
            } else {
                sb.append(('A'.toInt() + n - 36).toChar())
            }
        }
        return sb.toString()
    }

    @Throws(AudioConversionException::class)
    fun convert(f: File): File {
        val wav = File(GlobalVars.ITEM_DIR + "\\" + f.name.replace(".mp3", "") + ".wav")
        if (wav.exists()) {
            print("WAV file already exists, skipping conversion", CliType.WARNING)
            return wav
        }
        print("Converting " + f.absolutePath + " to " + wav.absolutePath)
        val audio = AudioAttributes()
        audio.setCodec("pcm_s16le")
        audio.setBitRate(320000)
        audio.setChannels(2)
        audio.setSamplingRate(44100)
        val attrs = EncodingAttributes()
        attrs.setFormat("wav")
        attrs.setAudioAttributes(audio)
        val encoder = Encoder()
        try {
            encoder.encode(f, wav, attrs)
        } catch (e: IllegalArgumentException) {
            print("Error converting " + f.absolutePath + " to " + wav.absolutePath, CliType.ERROR)
            print(e.message, CliType.ERROR)
        } catch (e: InputFormatException) {
            print("Error converting " + f.absolutePath + " to " + wav.absolutePath, CliType.ERROR)
            print(e.message, CliType.ERROR)
        } catch (e: EncoderException) {
            print("Error converting " + f.absolutePath + " to " + wav.absolutePath, CliType.ERROR)
            print(e.message, CliType.ERROR)
        }
        return wav
    }
}