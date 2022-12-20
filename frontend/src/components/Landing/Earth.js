import React, { useEffect, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function Earth() {
  const { scene } = useGLTF(require('assets/Models/earth_with_tree.glb'));
  const earth = useRef();
  useEffect(() => {
    earth.current.rotation.y += 400;
  }, []);

  useFrame(() => {
    // dandle.current.rotation.y += 0.005;
    if (earth.current.position.y < 70) earth.current.position.y += 0.9;
  });

  return (
    <instancedMesh ref={earth}>
      <primitive position={[7, -250, 0]} object={scene} scale={10} />{' '}
    </instancedMesh>
  );
}
