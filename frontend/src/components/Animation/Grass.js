import React, { useState, useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function Grass({ downgrass }) {
  const { scene } = useGLTF(require('assets/Models/grass.glb'));
  const land = useRef();

  useFrame(() => {
    if (downgrass) land.current.position.y -= 0.5;
  });

  useEffect(() => {
    //   console.log(downgrass);
  }, [downgrass]);

  return (
    <instancedMesh ref={land}>
      <primitive position={[0, -100, 0]} object={scene} scale={250} />
    </instancedMesh>
  );
}
