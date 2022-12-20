import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';
export default function DandelionSeedDown({ flag }) {
  const { scene } = useGLTF(require('assets/Models/seed_color.glb'));
  const [down, SetDown] = useState(1);
  const [size, SetSize] = useState(50);
  const [clicked, click] = useState(true);

  const seed = useRef();

  useFrame(() => {
    if (seed.current.position.y > -200) {
      seed.current.position.y -= down;
      SetSize(size + 0.01);
    } else {
      flag(true);
      SetSize(size + 0.01);
      seed.current.position.y -= down;
    }
  });

  useEffect(() => {
    seed.current.rotation.y += 0;
    seed.current.rotation.x += 0;
    seed.current.rotation.z -= 0.4;
  }, []);

  useEffect(() => {}, [size]);

  return (
    <instancedMesh ref={seed}>
      <primitive
        position={[-50, 100, -30]}
        object={scene}
        scale={6}
        onClick={(event) => {
          click(!clicked);
        }}
      />
    </instancedMesh>
  );
}
