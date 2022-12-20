import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function DandelionDown({ SeedUp, petal }) {
  const { scene } = useGLTF(require('assets/Models/two_dandel.glb'));
  const dandle = useRef();
  const [size, SetSize] = useState(9);
  useFrame(() => {
    if (petal && dandle.current.position.y > -100) {
      dandle.current.position.y -= 1;
      SetSize(size - 0.01);
    } else if (petal && dandle.current.position.y <= -100) {
      SeedUp(true);
    }
  });

  useEffect(() => {
    dandle.current.position.x -= 60;
    dandle.current.rotation.y += 0.7;
    dandle.current.rotation.x += 0.3;
    dandle.current.rotation.z += 0.4;
  }, []);
  return (
    <instancedMesh ref={dandle}>
      <primitive position={[-10, -180, 0]} object={scene} scale={size} />
    </instancedMesh>
  );
}
