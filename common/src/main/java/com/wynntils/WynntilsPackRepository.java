/*
 * Copyright © Wynntils 2023.
 * This file is released under AGPLv3. See LICENSE for full license details.
 */
package com.wynntils;

import java.util.List;

public interface WynntilsPackRepository {
    void setUsingWynntilsPack(boolean b);

    boolean isUsingWynntilsPack();

    void loadNewPackConfig(List<String> packsConfig);
}
