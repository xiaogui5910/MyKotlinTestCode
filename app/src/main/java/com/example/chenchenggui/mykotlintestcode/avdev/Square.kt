package com.example.chenchenggui.mykotlintestcode.avdev

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer


/**
 * description ：
 * author : chenchenggui
 * creation date: 2019/5/31
 */
class Square {
    private var vertexBuffer: FloatBuffer? = null
    private var drawListBuffer: ShortBuffer? = null

    // number of coordinates per vertex in this array
    val COORDS_PER_VERTEX = 3
    var squareCoords = floatArrayOf(-0.5f, 0.5f, 0.0f, // top left
            -0.5f, -0.5f, 0.0f, // bottom left
            0.5f, -0.5f, 0.0f, // bottom right
            0.5f, 0.5f, 0.0f) // top right

    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices

    constructor() {
        // initialize vertex byte buffer for shape coordinates
        val bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.size * 4)
        bb.order(ByteOrder.nativeOrder())
        vertexBuffer = bb.asFloatBuffer()
        vertexBuffer?.put(squareCoords)
        vertexBuffer?.position(0)

        // initialize byte buffer for the draw list
        val dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.size * 2)
        dlb.order(ByteOrder.nativeOrder())
        drawListBuffer = dlb.asShortBuffer()
        drawListBuffer?.put(drawOrder)
        drawListBuffer?.position(0)
    }
}