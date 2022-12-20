package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.dandelion.entity.Tag;

public interface TagService {
    Tag registerDandelionTag(Long dandelionSeq, Long memberSeq, String tag);
}
