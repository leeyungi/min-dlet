package com.i3e3.mindlet.domain.member.entity;

import java.io.Serializable;
import java.util.Objects;

public class MemberDandelionHistorySeq implements Serializable {

    private Long member;

    private Long dandelion;

    public MemberDandelionHistorySeq() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberDandelionHistorySeq that = (MemberDandelionHistorySeq) o;
        return Objects.equals(member, that.member) && Objects.equals(dandelion, that.dandelion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member, dandelion);
    }
}
