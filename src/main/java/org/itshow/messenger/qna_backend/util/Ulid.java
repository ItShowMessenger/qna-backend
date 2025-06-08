package org.itshow.messenger.qna_backend.util;

import de.huxhorn.sulky.ulid.ULID;
import org.springframework.stereotype.Component;

@Component
public class Ulid {
    private final ULID ulid = new ULID();

    public String nextUlid(){
        return ulid.nextULID();
    }
}
