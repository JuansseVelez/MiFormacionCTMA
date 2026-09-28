package com.ctma.miformacionctma.ui.screens.crear

import com.ctma.miformacionctma.domain.Prioridad
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class FormularioValidationTest {

    private fun validar(
        titulo: String,
        descripcion: String,
        fecha: String,
        prioridad: Prioridad = Prioridad.BAJA,
        progreso: Int = 0
    ): FormularioActividadUiState {
        val errorTitulo = when {
            titulo.isBlank() -> "El título es obligatorio"
            titulo.length < 3 -> "Mínimo 3 caracteres"
            titulo.length > 80 -> "Máximo 80 caracteres"
            else -> null
        }

        val errorDescripcion = if (descripcion.length > 240) "Máximo 240 caracteres" else null

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US).apply { isLenient = false }
        val errorFecha = try {
            val parsedDate = sdf.parse(fecha)
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            if (parsedDate != null && parsedDate.before(today)) {
                "La fecha no puede ser anterior a hoy"
            } else null
        } catch (_: Exception) {
            if (fecha.isNotBlank()) "Formato inválido (AAAA-MM-DD)" else "La fecha es obligatoria"
        }

        val errorProgreso = if (progreso !in 0..100) "Rango: 0-100" else null

        val puedeGuardar = errorTitulo == null && errorFecha == null &&
                errorDescripcion == null && errorProgreso == null &&
                titulo.isNotBlank() && fecha.isNotBlank()

        return FormularioActividadUiState(
            titulo = titulo,
            descripcion = descripcion,
            fecha = fecha,
            prioridad = prioridad,
            progreso = progreso,
            errorTitulo = errorTitulo,
            errorDescripcion = errorDescripcion,
            errorFecha = errorFecha,
            errorProgreso = errorProgreso,
            puedeGuardar = puedeGuardar
        )
    }

    @Test
    fun tituloEnBlancoProduceErrorYNoPuedeGuardar() {
        val state = validar("", "Descripción", "2026-12-31")
        assertEquals("El título es obligatorio", state.errorTitulo)
        assertFalse(state.puedeGuardar)
    }

    @Test
    fun tituloCortoProduceError() {
        val state = validar("AB", "Descripción", "2026-12-31")
        assertEquals("Mínimo 3 caracteres", state.errorTitulo)
        assertFalse(state.puedeGuardar)
    }

    @Test
    fun tituloExcesivamenteLargoProduceError() {
        val tituloLargo = "A".repeat(81)
        val state = validar(tituloLargo, "Descripción", "2026-12-31")
        assertEquals("Máximo 80 caracteres", state.errorTitulo)
        assertFalse(state.puedeGuardar)
    }

    @Test
    fun descripcionSupera240CaracteresProduceError() {
        val descLarga = "B".repeat(241)
        val state = validar("Título Válido", descLarga, "2026-12-31")
        assertEquals("Máximo 240 caracteres", state.errorDescripcion)
        assertFalse(state.puedeGuardar)
    }

    @Test
    fun fechaInvalidaProduceErrorDeFormato() {
        val state = validar("Título Válido", "Desc", "31-12-2026")
        assertEquals("Formato inválido (AAAA-MM-DD)", state.errorFecha)
        assertFalse(state.puedeGuardar)
    }

    @Test
    fun fechaAnteriorAHoyProduceError() {
        val state = validar("Título Válido", "Desc", "2020-01-01")
        assertEquals("La fecha no puede ser anterior a hoy", state.errorFecha)
        assertFalse(state.puedeGuardar)
    }

    @Test
    fun progresoFueraDeRangoProduceError() {
        val state = validar("Título Válido", "Desc", "2026-12-31", progreso = 150)
        assertEquals("Rango: 0-100", state.errorProgreso)
        assertFalse(state.puedeGuardar)
    }

    @Test
    fun formularioValidoEstablecePuedeGuardarEnTrue() {
        val state = validar("Título Excelente", "Descripción precisa", "2026-12-31", Prioridad.ALTA, 50)
        assertNull(state.errorTitulo)
        assertNull(state.errorDescripcion)
        assertNull(state.errorFecha)
        assertNull(state.errorProgreso)
        assertTrue(state.puedeGuardar)
    }
}
