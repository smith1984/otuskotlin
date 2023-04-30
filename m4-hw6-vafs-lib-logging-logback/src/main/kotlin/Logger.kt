package ru.beeline.vafs.logging.logback

import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory
import ru.beeline.vafs.logging.common.ILogWrapper
import kotlin.reflect.KClass

fun loggerLogback(logger: Logger): ILogWrapper = LogWrapperLogback(
    logger = logger,
    loggerId = logger.name,
)

fun loggerLogback(clazz: KClass<*>): ILogWrapper = loggerLogback(LoggerFactory.getLogger(clazz.java) as Logger)
@Suppress("unused")
fun loggerLogback(loggerId: String): ILogWrapper = loggerLogback(LoggerFactory.getLogger(loggerId) as Logger)
