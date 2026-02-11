package com.example.petcare.util

import android.content.Context
import android.location.Geocoder
import android.os.Build
import android.util.Log
import kotlinx.coroutines.Dispatchers // Importação necessária
import kotlinx.coroutines.withContext // Importação necessária
import java.io.IOException

data class Coords(val latitude: Double, val longitude: Double)

object CepGeocodingService {
    /**
     * Traduz um CEP em coordenadas (Latitude e Longitude) usando o Geocoder nativo do Android.
     * @return Coords com a localização, ou Coords(0.0, 0.0) em caso de falha.
     */
    suspend fun getCoordsFromCep(context: Context, cep: String): Coords {
        val cleanCep = cep.replace(Regex("[^0-9]"), "")
        // Se o CEP tiver menos de 8 dígitos, não é válido. Retorna fallback (0.0, 0.0)
        if (cleanCep.length < 8) return Coords(0.0, 0.0)

        // 0.0, 0.0 é o Golfo da Guiné. Usado para profissionais que não puderam ser geocodificados.
        val fallbackCoords = Coords(latitude = 0.0, longitude = 0.0)

        // 🎯 CORREÇÃO CRÍTICA: Executa o código de Geocoding na Thread de I/O.
        return withContext(Dispatchers.IO) {
            try {
                // É recomendado instanciar o Geocoder com o contexto.
                // Usar a localização (Locale) explícita é recomendado, mas é opcional.
                val geocoder = Geocoder(context)

                // Tenta obter o endereço a partir do CEP.
                val addresses = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    // Para Android 13 (API 33) e superior: método assíncrono recomendado.
                    // Nota: getFromLocationName() é síncrono no Geocoder nativo,
                    // mas requer ser chamado fora da Main Thread, por isso o Dispatchers.IO.
                    geocoder.getFromLocationName(cleanCep, 1)
                } else {
                    // Para APIs mais antigas: método síncrono depreciado.
                    @Suppress("DEPRECATION")
                    geocoder.getFromLocationName(cleanCep, 1)
                }

                if (!addresses.isNullOrEmpty()) {
                    val address = addresses[0]
                    Log.d("CEP_GEO", "CEP $cep geocodificado com sucesso: Lat=${address.latitude}, Lon=${address.longitude}")
                    Coords(address.latitude, address.longitude)
                } else {
                    Log.e("CEP_GEO", "CEP $cep não encontrado pelo Geocoder.")
                    fallbackCoords
                }
            } catch (e: IOException) {
                // Erro de rede ou indisponibilidade do serviço Geocoder
                Log.e("CEP_GEO", "Erro de IO/Rede ao usar Geocoder para CEP $cep.", e)
                fallbackCoords
            } catch (e: Exception) {
                // Inclui a SecurityException que pode ocorrer se o serviço Geocoder estiver inativo/mal configurado.
                Log.e("CEP_GEO", "Erro desconhecido ao usar Geocoder. Inclui SecurityException se Geocoder indisponível.", e)
                fallbackCoords
            }
        }
    }
}