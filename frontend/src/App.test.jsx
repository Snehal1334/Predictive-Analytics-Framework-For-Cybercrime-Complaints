import { render, screen } from '@testing-library/react';
import { describe, expect, it } from 'vitest';
import StatCard from './components/StatCard';

describe('StatCard', () => {
  it('renders label and value', () => {
    render(<StatCard label="Total complaints" value={12} />);
    expect(screen.getByText('Total complaints')).toBeInTheDocument();
    expect(screen.getByText('12')).toBeInTheDocument();
  });
});
