package org.pixelgalaxy.frameworks.registry.interfaces;

import org.pixelgalaxy.frameworks.registry.enums.LogType;

public interface Loggable {
    void log(LogType logType, String message);
}
