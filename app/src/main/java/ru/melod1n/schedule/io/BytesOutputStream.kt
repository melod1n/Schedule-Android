package ru.melod1n.schedule.io

import java.io.ByteArrayOutputStream

class BytesOutputStream : ByteArrayOutputStream {

    constructor() : super(8192)

    constructor(size: Int) : super(size)

    fun getByteArray() = buf

}