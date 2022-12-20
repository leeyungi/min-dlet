import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';
export default function DandelionPetalDown({ flag }) {
  const { scene } = useGLTF(require('assets/Models/petal.glb'));
  const [up, SetUp] = useState(0.8);
  const [size, SetSize] = useState(500);
  const [clicked, click] = useState(true);

  const petal = useRef();

  useFrame(() => {
    if (size > 30) {
      SetSize(size - 3.4);
      petal.current.position.x += 0.7;
      petal.current.position.y -= 1.1;
    } else {
      SetSize(size - 0.01);
      if (petal.current.position.y > -140) petal.current.position.y -= 0.5;
      else {
        flag(true);
      }
    }
    //  else {
    //   if (clicked && size < 600) {
    //     SetSize(size + 3);
    //     petal.current.position.x -= 0.5;
    //     petal.current.position.y += 0.6;
    //     petal.current.position.z += 0.2;
    //     petal.current.rotation.y += 0.003;
    //   } else if (size >= 600) {
    //     flag(1);
    //   }
    // }
  });

  useEffect(() => {
    petal.current.rotation.y += 0.5;
  }, []);

  return (
    <instancedMesh ref={petal} opacity={0}>
      <primitive
        position={[-100, 90, -40]}
        object={scene}
        scale={size}
        onClick={(event) => {
          click(!clicked);
        }}
      />
    </instancedMesh>
  );
}
