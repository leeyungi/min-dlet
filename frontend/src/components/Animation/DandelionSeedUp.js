import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';
export default function DandelionSeedUp({ flag }) {
  const { scene } = useGLTF(require('assets/Models/seed_color.glb'));
  const [down, SetDown] = useState(false);
  const [size, SetSize] = useState(50);
  const [clicked, click] = useState(true);

  const petal = useRef();

  useFrame(() => {
    if (down && petal.current.position.y < 380) petal.current.position.y += 4;
    else if (petal.current.position.y >= 380) {
      flag(true);
    }
  });

  setTimeout(() => SetDown(true), 1000);

  useEffect(() => {}, [size]);

  return (
    <instancedMesh ref={petal}>
      <primitive position={[-20, 0, -30]} object={scene} scale={6} />
    </instancedMesh>
  );
}
