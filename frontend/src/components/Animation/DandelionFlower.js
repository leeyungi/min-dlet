import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function Earth() {
  const { scene } = useGLTF(require('assets/Models/two_dandel.glb'));
  const dandle = useRef();
  // useFrame(() => (dandle.current.rotation.y += 0.005));

  useEffect(() => {
    dandle.current.position.x -= 60;
    dandle.current.rotation.y += 0.7;
    dandle.current.rotation.x += 0.3;
    dandle.current.rotation.z += 0.4;
  }, []);

  return (
    <instancedMesh ref={dandle}>
      <primitive position={[-10, -180, 0]} object={scene} scale={9} />
    </instancedMesh>
  );
}
