package com.i3e3.mindlet.domain.dandelion.service;

import com.i3e3.mindlet.domain.admin.entity.Report;

public interface PetalService {

    Report reportPetal(Long memberSeq, Long petalSeq, Report.Reason reason);

    boolean isDandelionOwnerByPetal(Long memberSeq, Long petalSeq);

    void deletePetal(Long petalSeq);
}
