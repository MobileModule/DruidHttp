package com.druid.http.tools;

import java.util.LinkedHashMap;
import java.util.List;

public class LinkedMultiValueMap<K, V> extends BasicMultiValueMap<K, V> {

    public LinkedMultiValueMap() {
        super(new LinkedHashMap<K, List<V>>());
    }
}
