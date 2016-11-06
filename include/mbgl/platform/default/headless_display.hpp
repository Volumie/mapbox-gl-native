#pragma once

#include <mbgl/gl/implementation.hpp>

#include <memory>

namespace mbgl {

class HeadlessDisplay {
public:
    HeadlessDisplay();
    ~HeadlessDisplay();

    template <typename PixelFormat>
    PixelFormat pixelFormat() const;

private:
    class Impl;
    std::unique_ptr<Impl>  impl;
};

} // namespace mbgl
