import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';
export default function DandelionPetalUp({ flag }) {
  const { scene } = useGLTF(require('assets/Models/petal.glb'));
  const [up, SetUp] = useState(0.8);
  const [size, SetSize] = useState(50);
  const [clicked, click] = useState(true);

  const petal = useRef();

  useFrame(() => {
    if (petal.current.position.y < 80) {
      petal.current.position.y += up;
      SetSize(size + 0.01);
    } else {
      if (clicked && size < 600) {
        SetSize(size + 3);
        petal.current.position.x -= 0.5;
        petal.current.position.y += 0.6;
        petal.current.position.z += 0.2;
        petal.current.rotation.y += 0.003;
      } else if (size >= 600) {
        flag(1);
      }
    }
  });

  useEffect(() => {}, [size]);

  return (
    <instancedMesh ref={petal}>
      <primitive position={[-20, -50, 0]} object={scene} scale={size} />
    </instancedMesh>
  );
}
