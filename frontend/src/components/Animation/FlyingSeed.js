import React, { useEffect, useMemo, useRef } from 'react';
import { useGLTF } from '@react-three/drei';
import { useFrame } from '@react-three/fiber';

export default function FlyingSeed({ seed }) {
  const { scene } = useGLTF(require('assets/Models/dandl_compressed.glb'));
  const [copiedScene, globalValue, globalValue2] = useMemo(
    () => [
      scene.clone(),
      parseInt(Math.random(seed) * 50 - 25),
      parseInt(Math.random(seed) * 50 - 25),
    ],
    [scene]
  );
  const dandle = useRef();
  // const interval = Math.max(2000, globalValue * 1000)
  let rand = Math.random(seed) - 0.5;
  setInterval(() => {
    rand = Math.random(seed) - 0.5;
  }, Math.max((globalValue + 10) * 1000, 4000));

  useFrame((state) => {
    const time = state.clock.getElapsedTime();
    dandle.current.rotation.x = (time * rand) / 10;
    dandle.current.rotation.y = (-time * rand) / 100;
    dandle.current.rotation.z = (time * rand) / 10;
    dandle.current.position.x += rand / 4;
    dandle.current.position.y += rand / 4;
    dandle.current.position.z += rand / 4;
  });

  return (
    <instancedMesh
      position={[
        globalValue * 3 - globalValue2 * 3 + 25,
        globalValue * 3 + globalValue2 * 3 + 30,
        globalValue * 4 - globalValue2 * 2,
      ]}
      ref={dandle}
    >
      <primitive object={copiedScene} scale={2} />
    </instancedMesh>
  );
}
